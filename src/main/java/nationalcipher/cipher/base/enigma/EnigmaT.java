package nationalcipher.cipher.base.enigma;

public class EnigmaT extends EnigmaMachine {

    public EnigmaT(String name) {
        super(name);
        this.setRotors("KPTYUELOCVGRFQDANJMBSWHZXI", "UPHZLWEQMTDJXCAKSOIGVBYFNR", "QUDLYRFEKONVZAXWHMGPJBSICT", "CIWTBKXNRESPFLYDAGVHQUOJZM", "UAXGISNJBVERDYLFZWTPCKOHMQ", "XFUZGALVHCNYSEWQTDMRBKPIOJ", "BJVFTXPLNAYOZIKWGDQERUCHSM", "YMTPNZHWKODAJXELUQVGCBISFR");
        this.setNotches("WZEKQ", "WZFLR", "WZEKQ", "WZFLR", "YCFKR", "XEIMQ", "YCFKR", "XEIMQ");
        this.setReflectors("GEKPBTAUMOCNILJDXZYFHWVQSR");
        this.setReflectorNames("UKW");
        this.setETW("KZROUQHYAIGBLWVSTDXFPNMCJE");
    }
}
