package com.example.analytics;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class AnalyticsApplication {

    static final String PAGE_COUNT_MV = "pcmv";

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsApplication.class, args);
    }
/*
    // this is S C Kafka
    @Bean
     Supplier<PageViewEvent> pageViewEventSupplier() {
        var names = List.of("mfisher", "dyser", "schacko", "abilan", "ozhurakousky", "grussell");
        var pages = List.of("blog", "sitemap", "initializr", "news", "colophon", "about");
        return () -> {
            var rPage = pages.get(new Random().nextInt(pages.size()));
            var rName = pages.get(new Random().nextInt(names.size()));
            return new PageViewEvent(rName, rPage, Math.random() > .5 ? 10 : 1000);
        };
    }*/

    // these next two are S C Kafka Streams
    @Bean
    Function<KStream<String, PageViewEvent>, KStream<String, Long>> process() {
        return kStream -> kStream //
                .filter((key, value) -> value.duration() > 10) //
                .map((key, value) -> new KeyValue<>(value.page(), "0"))//
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String())) //
                .count(Materialized.as(AnalyticsApplication.PAGE_COUNT_MV))// rocksdb behind the scenes
                .toStream();
    }

    @Bean
    Consumer<KTable<String, Long>> pageCount() {
        return counts -> counts.toStream().foreach((key, value) -> log.info(key + "=" + value));
    }
}

@Controller
@ResponseBody
class CountRestController {

    private final StreamBridge streamBridge;

    CountRestController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/view")
    Map<String, PageViewEvent> counts() {
        var pageViewEvent = random();
        // make sure to specify the `outputBindings` business, otherwise use `pvs` in this param
        var sent = this.streamBridge.send(
                "pageViewEventSupplier-out-0", pageViewEvent);
        Assert.state(sent, "the " + pageViewEvent + " has not been sent");
        return Map.of("message", pageViewEvent);
    }

    private PageViewEvent random() {
        var names = List.of("mfisher", "dyser", "schacko", "abilan", "ozhurakousky", "grussell");
        var pages = List.of("blog", "sitemap", "initializr", "news", "colophon", "about");
        var rPage = pages.get(new Random().nextInt(pages.size()));
        var rName = pages.get(new Random().nextInt(names.size()));
        return new PageViewEvent(rName, rPage, Math.random() > .5 ? 10 : 1000);
    }
}

record PageViewEvent(String userId, String page, long duration) {
}