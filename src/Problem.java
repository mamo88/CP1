import java.util.HashMap;
import java.util.Vector;

public class Problem {

    public int _n;
    public int _k;
    public double _p1;
    public double _p2;
    public Vector<Integer> _variables;
    public Vector<Vector<Integer>> _domains;
    public Vector<Vector<HashMap<String, Boolean>>> _constraints;
    public boolean _isSolved;
    public int _ccs;
    public int _assignments;

    public Problem(int n, int d, double p1, double p2) {
        this._n = n;
        this._k = d;
        this._p1 = p1;
        this._p2 = p2;
        initialize();
        createRandomConstraints();
    }

    public Problem(Problem other){
        this._n = other._n;
        this._k = other._k;
        this._p1 = other._p1;
        this._p2 = other._p2;

        this._variables = new Vector<Integer>(this._n);
        for (int i = 0; i < this._n; i++) {
            this._variables.add(i, other._variables.get(i));
        }

        this._domains = new Vector<Vector<Integer>>(this._n);
        Vector<Integer> domain;
        for (int i = 0; i < this._n; i++) {
            domain = new Vector<Integer>(this._k);
            for (int j = 0; j < this._k; j++) {
                domain.add(j, other._domains.get(i).get(j));
            }
            this._domains.add(domain);
        }

        this._constraints = new Vector<Vector<HashMap<String, Boolean>>>(this._n);
        Vector<HashMap<String, Boolean>> vector;
        HashMap<String, Boolean> hashMap;
        for (int i = 0; i < this._n; i++) {
            vector = new Vector<HashMap<String, Boolean>>(this._n);
            for (int j = 0; j < this._n; j++) {
                hashMap = new HashMap<String, Boolean>((int)Math.pow(this._k, 2));
                for (int assi = 0; assi < this._k; assi++) {
                    for (int assj = 0; assj < this._k; assj++) {
                        if (other._constraints.get(i).get(j).get((new Pair(assi, assj)).key()) == false)
                            hashMap.put((new Pair(assi,assj)).key(), false);
                        else
                            hashMap.put((new Pair(assi,assj)).key(), true);
                    }
                }
                vector.add(hashMap);
            }
            this._constraints.add(vector);
        }
    }

    public void initialize() {
        this._isSolved = false;
        this._ccs = 0;
        this._assignments = 0;

        this._variables = new Vector<Integer>(this._n);
        for (int i = 0; i < this._n; i++) {
            this._variables.add(0);
        }
        this._domains = new Vector<Vector<Integer>>(this._n);
        Vector<Integer> domain;
        for (int i = 0; i < this._n; i++) {
            domain = new Vector<Integer>(this._k);
            for (int j = 0; j < this._k; j++) {
                domain.add(j);
            }
            this._domains.add(domain);
        }
    }

    public void createRandomConstraints() {
        this._constraints = new Vector<Vector<HashMap<String, Boolean>>>(this._n);
        Vector<HashMap<String, Boolean>> vector;
        HashMap<String, Boolean> hashMap;
        boolean isConstraint;
        for (int i = 0; i < this._n; i++) {
            vector = new Vector<HashMap<String, Boolean>>(this._n);
            for (int j = 0; j < this._n; j++) {
                hashMap = new HashMap<String, Boolean>((int)Math.pow(this._k, 2));
                isConstraint = (i != j) && (Math.random() <= this._p1);
                for (int assi = 0; assi < this._k; assi++) {
                    for (int assj = 0; assj < this._k; assj++) {
                        if (isConstraint && (Math.random() <= this._p2))
                            hashMap.put((new Pair(assi,assj)).key(), false);
                        else
                            hashMap.put((new Pair(assi,assj)).key(), true);
                    }
                }
                vector.add(hashMap);
            }
            this._constraints.add(vector);
        }
    }

    public boolean check(int v1, int ass1, int v2, int ass2) {
        this._ccs++;
        return this._constraints.get(v1).get(v2).get((new Pair(ass1,ass2)).key());
    }

    public void assignVariable(int var, Integer val) {
        this._assignments++;
        this._variables.set(var, val);
    }

    public String solutionString() {
        String sol = "";
        sol += "Assignment = ";
        for (int i = 0; i < this._n; i++) {
            sol += "<" + i + "," + this._variables.get(i) + ">,";
        }
        sol += "CCs=" + this._ccs + ",Assignments=" + this._assignments;
        return sol;
    }

    @Override
    public String toString() {
        return "N=" + this._n + ",D=" + this._k + ",P1=" + this._p1 + ",P2=" + this._p2;
    }
}


