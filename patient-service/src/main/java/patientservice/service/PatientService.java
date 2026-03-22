package patientservice.service;

import org.springframework.stereotype.Service;
import patientservice.DTO.PatientRequestDTO;
import patientservice.DTO.PatientResponseDTO;
import patientservice.exception.EmailAlreadyExistsException;
import patientservice.exception.PatientNotFoundException;
import patientservice.grpc.BillingServiceGrpcClient;
import patientservice.kafka.kafkaProducer;
import patientservice.mapper.PatientMapper;
import patientservice.model.Patient;
import patientservice.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final kafkaProducer kafkaProducer;
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, kafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }



        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(),newPatient.getEmail() );
        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);

    }


    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("patient not found with ID: " + id));


        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);

    }


    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
