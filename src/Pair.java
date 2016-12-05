public class Pair {

    public int _assV1;
    public int _assV2;

    public Pair(int assV1, int assV2) {
        this._assV1 = assV1;
        this._assV2 = assV2;
    }

    public String key() {
        return this._assV1 + "" + this._assV2;
    }
}
