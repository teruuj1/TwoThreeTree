package twothreetree;

public class Main {
    public static void main(String[] args) {

        int[] massiv = new int[10000]; // заполняем массив
        for (int i = 0; i < massiv.length; ++i) {
            int b = random(2, 1000000);
            massiv[i] = b;
        }

        TwoThreeTree tree = new TwoThreeTree(1);

        //1
        double sum1 = 0;
        for (int k : massiv) {
            long start = System.nanoTime();
            tree.insert(k); //добавляем в дерево
            long end = System.nanoTime();
            sum1 += (end - start);
        }

        System.out.println("➣среднее время добавления элемента (наносекунды) - " + sum1/massiv.length);
        System.out.println("количесвто операций - " + tree.root.getCount());
        tree.root.setCount(0);

        //2
        double sum2 = 0;
        for (int i = 1; i <= 100; ++i) {
            int index = random(0, massiv.length-1);
            long start = System.nanoTime();
            tree.search(massiv[index]); //ищем элемент
            long end = System.nanoTime();
            sum2 += (end-start);
        }

        System.out.println("➣среднее время поиска элемента (наносекунды) - " + sum2/100);
        System.out.println("количесвто операций - " + tree.root.getCount());
        tree.root.setCount(0);

        //3
        double sum3 = 0;
        for (int i = 1; i <= 1000; ++i) {
            int index = random(0, massiv.length - 1);
            long start = System.nanoTime();
            tree.remove(massiv[index]); //удаляем элемент
            long end = System.nanoTime();
            sum3 += (end - start);
        }

        System.out.println("➣среднее время удаления элемента (наносекунды) - " + sum3/1000);
        System.out.println("количесвто операций - " + tree.root.getCount());
        tree.root.setCount(0);
    }


    public static int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}

