import java.util.Comparator;

public class lambda {
    public static void main(String[] args) {
        // Comparator<Integer> comparator = (x,y) -> {
        //     return x*y;
        // };

        Comparator<Object> comparator = new Comparator<>() {
            @Override
            public boolean equals(Object x) {
                return true;
            }

            @Override
            public int compare(Object o1, Object o2) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        System.out.println(comparator.equals(2));
    }

}
