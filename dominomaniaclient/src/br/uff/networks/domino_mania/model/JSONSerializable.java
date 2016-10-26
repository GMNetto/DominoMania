package br.uff.networks.domino_mania.model;

interface JSONSerializable {

    String toJSON();

    void fromJSON(String json);

}
