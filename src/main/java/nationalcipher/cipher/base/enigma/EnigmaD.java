package nationalcipher.cipher.base.enigma;

public class EnigmaD extends EnigmaMachine {

    public EnigmaD(String name) {
        super(name);
        this.setRotors("LPGSZMHAEOQKVXRFYBUTNICJDW", "SLVGBTFXJQOHEWIRZYAMKPCNDU", "CJGDPSHKTURAWZXFMYNQOBVLIE");
        this.setNotches(new Integer[][] { { 24 }, { 4 }, { 13 } });
        this.setReflectors("IMETCGFRAYSQBZXWLHKDVUPOJN");
        this.setReflectorNames("UKW");
        this.setETW("QWERTZUIOASDFGHJKPYXCVBNML");
    }

}
