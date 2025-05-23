package com.example.eip;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class EipApplication {

	public static void main(String[] args) {
		SpringApplication.run(EipApplication.class, args);
	}

	@Bean
	DirectChannelSpec orders() {
		return MessageChannels.direct();
	}

	@Bean
	IntegrationFlow filesIntegrationFlow(MessageChannel orders,
			@Value("file://${purchase-orders.directory}") File inputDir) {
		var files = Files.inboundAdapter(inputDir).autoCreateDirectory(true);
		return IntegrationFlow.from(files).channel(orders).get();
	}

	// 1. read .json files from
	// ${HOME}/Desktop/frontend-masters-spring-boot-course/messaging-integration/purchase-orders/
	// 1. read purchase-orders from an http inbound endpoint in tomcat
	// 1. connect via a MessageChannel
	// 2. transform into PurchaseOrder objects
	// 5. split PurchaseOrder objects into PurchaseOrderLineItems
	// 6. for each lineitem, enrich with whether the order is domestic or international
	// 7. 'ship' it
	// 8. aggregate the results
	// 9. write to RMQ

	@Bean
	IntegrationFlow ordersIntegrationFlow(AmqpTemplate amqpTemplate, MessageChannel orders) {
		return IntegrationFlow.from(orders)
			.transform(new JsonToObjectTransformer(PurchaseOrder.class))
			.split(PurchaseOrder.class, purchaseOrder -> {
				System.out.println("========================================");
				var set = new HashSet<ShippableLineItem>();
				for (var lineItem : purchaseOrder.lineItems()) {
					set.add(new ShippableLineItem(purchaseOrder, lineItem, "US".equals(purchaseOrder.country()),
							false));
				}
				return set;
			})
			.handle((GenericHandler<ShippableLineItem>) (payload, headers) -> {
				System.out.println("\tshipping " + (payload.domestic() ? "domestic" : "international") + ": " + payload
						+ " with heders : " + headers + ".");
				return new ShippableLineItem(payload.order(), payload.original(), payload.domestic(), true);
			})
			.aggregate(as -> as.correlationStrategy(message -> {
				var shippableLineItem = (ShippableLineItem) message.getPayload();
				return shippableLineItem.order().orderId();
			}))
			.handle((GenericHandler<Collection<ShippableLineItem>>) (payload, headers) -> {
				var order = payload.iterator().next().order();
				System.out.println("\torder: [" + order + "]");
				for (var sli : payload) {
					System.out.println("\t\t" + sli.original() + " shipped: " + sli.shipped() + ".");
				}
				return null;
			})
			.get();
	}

}

record ShippableLineItem(PurchaseOrder order, LineItem original, boolean domestic, boolean shipped) {
}

record LineItem(String sku, String productName, int quantity, double unitPrice) {
}

record PurchaseOrder(String orderId, String country, Set<LineItem> lineItems, double total) {
}

@Controller
@ResponseBody
class OrdersController {

	private final MessageChannel orders;

	OrdersController(MessageChannel orders) {
		this.orders = orders;
	}

	// http --form POST :9091/orders
	// order@~/Desktop/frontend-masters-spring-boot-course/messaging-integration/purchase-orders/1010.json
	@PostMapping("/orders")
	void orders(@RequestBody MultipartFile order) throws IOException {
		var content = order.getResource().getContentAsString(Charset.defaultCharset());
		var msg = MessageBuilder.withPayload(content).build();
		this.orders.send(msg);
	}

}
