package io.telehelth.authorizationserver.controller;

import io.telehelth.authorizationserver.entity.DoctorRoom;
import io.telehelth.authorizationserver.entity.DoctorRoomRepository;
import io.telehelth.authorizationserver.entity.TherapyGroup;
import io.telehelth.authorizationserver.entity.TherapyGroupRepository;
import io.telehelth.authorizationserver.service.Service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/video-api")
@CrossOrigin
public class Controller {
    private final Logger logger = Logger.getLogger(Controller.class.getName());
    private final Service service;
    private final WebClient videoApi;
    private final DoctorRoomRepository doctorRoomRepository;
    private final TherapyGroupRepository therapyGroupRepository;

    public Controller(Service service, WebClient videoApi, DoctorRoomRepository doctorRoomRepository, TherapyGroupRepository therapyGroupRepository) {
        this.service = service;
        this.videoApi = videoApi;
        this.doctorRoomRepository = doctorRoomRepository;
        this.therapyGroupRepository = therapyGroupRepository;
    }

    @GetMapping("/check-server")
    public String test(){
       return "yea it is Working";
    }


    @PostMapping("/create-room")
    @Transactional
    public ResponseEntity<DoctorRoom> createRoom(@RequestBody Map<String,String> body){
        String username = body.get("username");
        doctorRoomRepository.deleteByUsername(username);//make sure there is only one room per user
        String type = body.get("type"); //consultation,vdt,therapyGroup
        logger.info("creating a a room for doctor username: "+username+", with purpose of: "+type);
        String token = service.getToken();
        Map<String,Object> response = videoApi.post()
                .uri("/meetings")
                   .header("Authorization",token)
                   .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                   .retrieve()
                   .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() { })
                   .block();
        DoctorRoom doctorRoom = new DoctorRoom();
        doctorRoom.setUsername(username);
        doctorRoom.setRoomId(response.get("meetingId").toString());
        doctorRoom.setToken(token);
        doctorRoomRepository.save(doctorRoom);
        logger.info("room has been create with id : "+response.get("meetingId"));
        return ResponseEntity.ok(doctorRoom);
    }

    @GetMapping("/validate-room/{roomId}")
    public ResponseEntity<Void> validate(@PathVariable String roomId){
        String token = service.getToken();
       int status =  videoApi.post()
                .uri("/meetings/"+roomId)
                .header("Authorization",token)
                .retrieve()
                .toBodilessEntity()
                .block()
               .getStatusCodeValue();
       return ResponseEntity.status(status).build();
    }

    @GetMapping("/get-room/{username}")
    public ResponseEntity<DoctorRoom> getRoom(@PathVariable String username){
        return ResponseEntity.ok(doctorRoomRepository.findByUsername(username).get());
    }

    @GetMapping("/delete-room/{username}")
    @Transactional
    public ResponseEntity<Void> deleteRoom(@PathVariable String username){
        doctorRoomRepository.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-therapy-group")
    public ResponseEntity<TherapyGroup> createTherapyGroup(@RequestBody @Valid TherapyGroup therapyGroup){
        return ResponseEntity.ok(therapyGroupRepository.save(therapyGroup));
    }

    @GetMapping("/therapy-groups")
    public ResponseEntity<List<TherapyGroup>> getAllTherapyGroup(){
        return ResponseEntity.ok(therapyGroupRepository.findAll());
    }

    @GetMapping("/therapy-groups/doctor/{therapist}")
    public ResponseEntity<List<TherapyGroup>> getMyTherapyGroup(@PathVariable String therapist){
        return ResponseEntity.ok(therapyGroupRepository.findAllByTherapist(therapist));
    }

    @GetMapping("/therapy-groups/patient/{username}")
    public ResponseEntity<List<TherapyGroup>> getMyTherapyGroupPatient(@PathVariable String username){
        //this code is not correctly implemented we shouldn't do it like this instead we should utilize what is given for us for free instead of implementing it from the scratch.
        List<TherapyGroup> therapyGroupList = therapyGroupRepository.findAll()
                .stream()
                .filter(therapyGroup -> {
                    String[] patients = therapyGroup.getPatients();
                    return Arrays.stream(patients)
                            .anyMatch(e->e.equals(username));
                }).collect(Collectors.toList());

        return ResponseEntity.ok(therapyGroupList);
    }

    @DeleteMapping("/therapy-groups/{id}/{username}")
    public ResponseEntity<Void> deleteMyTherapyGroupPatient(@PathVariable long id,@PathVariable String username){
        TherapyGroup therapyGroup = therapyGroupRepository.findById(id).get();
        therapyGroup.setPatients(Arrays.stream(therapyGroup.getPatients())
                .filter(e -> !e.equals(username)).toArray(String[]::new));
        therapyGroupRepository.save(therapyGroup);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/join-therapy-group/{id}/{username}")
    public ResponseEntity<Void> joinTherapyGroup(@PathVariable long id,@PathVariable String username){
        TherapyGroup therapyGroup = therapyGroupRepository.findById(id).get();
        String[] currPatients = therapyGroup.getPatients();

        List<String> patientList = Arrays.stream(currPatients)
                .collect(Collectors.toList())
                ;
        patientList.add(username);
        therapyGroup.setPatients(patientList.toArray(new String[0]));
        therapyGroupRepository.save(therapyGroup);
        return ResponseEntity.ok().build();
    }

}
