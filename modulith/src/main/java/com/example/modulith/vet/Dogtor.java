package com.example.modulith.vet;

import com.example.modulith.adoptions.DogAdoptionEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class Dogtor {

    @ApplicationModuleListener
    void adopt(DogAdoptionEvent event) throws Exception {
        Thread.sleep(5000);
        System.out.println("Adopting dog " + event.dogId() + "!");
    }
}
