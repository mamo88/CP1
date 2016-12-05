import java.util.Vector;

public class BMCBJ {
    public enum Status{
        INITIALIZED, UNKNOWN, UNINITIALIZED, SOLVED, IMPOSSIBLE
    }
    public boolean _consistent;
    public Vector<Vector<Integer>> _currentDomain;
    public Status _status;
    public Problem _problem;
    public Vector<Vector<Integer>> _mcl;
    public Vector<Integer> _mbl;
    public Vector<Vector<Integer>> _confSet;

    public BMCBJ (){
        this._status = Status.UNINITIALIZED;
    }

    public void init(Problem problem){
        this._problem = problem;
        this._currentDomain = new Vector<>();
        for (int i = 0; i < problem._n; i++){
            _currentDomain.add(problem._domains.get(i));
        }
        this._confSet = new Vector<>(problem._n);
        this._mbl = new Vector<>(problem._n);
        this._mcl = new Vector<>(problem._n);
        Vector<Integer> vec;
        for (int i = 0; i < problem._n; i++){
            vec = new Vector<Integer>(problem._k);
            for (int j = 0; j < problem._k; j++){
                vec.add(j, 0);
            }
            this._mcl.add(vec);
            this._confSet.add(new Vector<Integer>());
            this._mbl.add(i, 0);
        }
        this._status = Status.INITIALIZED;
    }

    public void solve(Problem problem){
        init(problem);
        solve();
    }

    public void solve(){
//        this._problem.initDataStructures();
        this._consistent = true;
        this._status = Status.UNKNOWN;
        int i = 0;
        while (this._status == Status.UNKNOWN){
            if (this._consistent)
                i = label(i);
            else
                i = unlabel(i);
            if (i >= this._problem._n){
                this._status = Status.SOLVED;
                this._problem._isSolved = true;
            }
            else if (i <= 0)
                this._status = Status.IMPOSSIBLE;
        }
    }

    public int label (int i){
        this._consistent = false;
        int j = 0;
        int k, h;
        while (!this._currentDomain.get(i).isEmpty() && !this._consistent){
            k = this._currentDomain.get(i).get(j); //try to assign var i the value k from it's domain
            this._consistent = this._mcl.get(i).get(k) >= this._mbl.get(i);
            h = this._mbl.get(i);
            while (this._consistent && h < (i-1)){
                this._problem._variables.set(i, k);
                this._consistent = this._problem.check(i, this._problem._variables.get(i), h, this._problem._variables.get(h));
                this._mcl.get(i).set(k, h);
                h++;
            }
            if (!this._consistent){
                this._confSet.get(i).add(this._mcl.get(i).get(k));
                this._currentDomain.get(i).remove(j);
            }
            j++;
        }
        if (this._consistent)
            return i+1;
        else
            return i;
    }

    public int unlabel(int i) {
        int h = maxList(this._confSet.get(i)); // h is the "deepest" var in i's conflict set -> we return to him
        this._confSet.set(h, union(this._confSet.get(h), this._confSet.get(i)));
        this._confSet.get(h).removeElement(h);
        this._mbl.set(i, h);
        for (int j = (h + 1); j < this._problem._n; j++) {
            this._mbl.set(j, Math.min(this._mbl.get(j), h));
        }
        for (int j = (h + 1); j < i; j++) {
            this._confSet.get(j).removeAllElements();
            this._currentDomain.set(j, this._problem._domains.get(j));
        }
        this._currentDomain.get(h).removeElement(this._problem._variables.get(h));
        this._consistent = this._currentDomain.get(h).isEmpty();
        return h;
    }

    public int maxList(Vector<Integer> confSet){
        int max = 0;
        for (int i = 0; i < confSet.size(); i++){
            max = Math.max(max, confSet.get(i));
        }
        return max;
    }

    public Vector<Integer> union(Vector<Integer> a, Vector<Integer> b){
        Vector ret = new Vector<Integer>();
        ret.addAll(a);
        ret.addAll(b);
        return ret;
    }
}
