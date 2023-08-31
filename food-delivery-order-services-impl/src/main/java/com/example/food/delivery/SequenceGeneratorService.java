package com.example.food.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {

    @Autowired
    private SequenceRepository sequenceRepository;

    public long generateSequence(String sequenceName) {
        DatabaseSequence sequence = sequenceRepository.findById(sequenceName)
                .orElse(new DatabaseSequence(sequenceName, 1));

        long nextValue = sequence.getSeq();
        sequence.setSeq(nextValue + 1);
        sequenceRepository.save(sequence);

        return nextValue;
    }
}
