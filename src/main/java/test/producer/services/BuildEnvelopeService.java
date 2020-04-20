package test.producer.services;

import test.producer.Utils.EnvelopeParameters;

public interface BuildEnvelopeService {
    Boolean buildEnvelope(EnvelopeParameters envelopeParameters) throws Exception;
}
