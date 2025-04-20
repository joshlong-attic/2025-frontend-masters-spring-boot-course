package com.example.modulith.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
class DogAdoptionController {

	private final DogAdoptionService dogAdoptionService;

	DogAdoptionController(DogAdoptionService dogAdoptionService) {
		this.dogAdoptionService = dogAdoptionService;
	}

	@PostMapping("/dogs/{dogId}/adoptions")
	void adopt(@PathVariable int dogId, @RequestParam String owner) {
		this.dogAdoptionService.adopt(dogId, owner);
	}

}

@Service
@Transactional
class DogAdoptionService {

	private final DogRepository repository;

	private final ApplicationEventPublisher applicationEventPublisher;

	DogAdoptionService(DogRepository repository, ApplicationEventPublisher applicationEventPublisher) {
		this.repository = repository;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	void adopt(int id, String owner) {
		repository.findById(id).ifPresent(dog -> {
			var up = this.repository.save(new Dog(dog.id(), dog.name(), owner, dog.description()));
			System.out.println("Adopted [" + up.name() + "] to [" + owner + "].");
			this.applicationEventPublisher.publishEvent(new DogAdoptionEvent(up.id()));
		});
	}

}

record Dog(@Id int id, String name, String owner, String description) {
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {

}