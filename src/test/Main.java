package test;

public class Main {

    static class Value{
        int i;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.first();
    }

    private void first() {
        int i = 5;
        Value v = new Value();
        v.i = 25;
        second(v, i);
        System.out.println(v.i);
    }

    private void second(Value v, int i) {
        i = 0;
        v.i = 20;
        Value val = new Value();
        v = val;
        System.out.println(v.i + "" + i);
    }

}
