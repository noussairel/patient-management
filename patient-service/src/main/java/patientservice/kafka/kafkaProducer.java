package patientservice.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import patientservice.model.Patient;

@Slf4j
@Service
public class kafkaProducer {
    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String,byte[]> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient){
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {

            kafkaTemplate.send("patient", patientEvent.toByteArray());

        }catch (Exception ex){
            log.error("Error sending PatientCreated Event: {}", patientEvent);
        }

    }
}
