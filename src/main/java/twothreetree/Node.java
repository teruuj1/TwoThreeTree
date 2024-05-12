package twothreetree;

public class Node {

    private int size;
    private int[] key = new int[3];
    private Node first;   // first <= key[0]
    private Node second;  // key[0] <= second < key[1]
    private Node third;   // key[1] <= third < key[2]
    private Node fourth;  // key[2] <= fourth
    private Node parent;  // Указатель на родителя нужен, потому что адрес корня может меняться при удалении
    private int count = 0;

    public Node(int k) {
        this.size = 1;
        this.key[0] = k;
        this.first = null;
        this.second = null;
        this.third = null;
        this.fourth = null;
        this.parent = null;
        count++;
    }

    public Node(int k, Node first, Node second, Node third, Node fourth, Node parent) {
        this.size = 1;
        this.key[0] = k;
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.parent = parent;
        count++;
    }


    public int getSize() {
        return size;
    }

    public int getKey(int i) {
        return key[i];
    }

    public Node getFirst() {
        return first;
    }

    public Node getSecond() {
        return second;
    }

    public Node getThird() {
        return third;
    }

    public Node getFourth() {
        return fourth;
    }

    public Node getParent() {
        return parent;
    }

    public int getCount() {
        return count;
    }


    public void setSize(int size) {
        this.size = size;
        count++;
    }

    public void setKey(int i, int key_) {
        this.key[i] = key_;
        count++;
    }

    public void setFirst(Node first) {
        this.first = first;
        count++;
    }

    public void setSecond(Node second) {
        this.second = second;
        count++;
    }

    public void setThird(Node third) {
        this.third = third;
        count++;
    }

    public void setFourth(Node fourth) {
        this.fourth = fourth;
        count++;
    }

    public void setParent(Node parent) {
        this.parent = parent;
        count++;
    }

    public void setCount(int count) {
        this.count = count;
    }


    private boolean find(int k) {
        for (int i = 0; i < size; i++) {
            if (key[i] == k) {
                return true;
            }
        }
        return false;
    }

    private void swap(int x, int y) {
        int r = x;
        x = y;
        y = r;
        count++;
    }

    private void sort2(int x, int y) {
        if (x > y) swap(x, y);
    }

    private void sort3(int x, int y, int z) {
        if (x > y) swap(x, y);
        if (x > z) swap(x, z);
        if (y > z) swap(y, z);
    }

    private void sort() { // Ключи в вершинах должны быть отсортированы
        if (size == 1) return;
        if (size == 2) sort2(key[0], key[1]);
        if (size == 3) sort3(key[0], key[1], key[2]);
    }

    private void insertToNode(int k) {
        key[size] = k;
        size++;
        sort();
    }

    private void removeFromNode(int k) {
        for (int i = 0; i < size; i++) {
            if (key[i] == k) {
                System.arraycopy(key, i + 1, key, i, size - i - 1);
                key[size - 1] = 0;
                size--;
                count++;
                break;
            }
        }
    }

    private void becomeNode2(int k, Node first, Node second) {
        key[0] = k;
        this.first = first;
        this.second = second;
        third = null;
        fourth = null;
        parent = null;
        size = 1;
        count++;
    }

    private boolean isLeaf() {
        return first == null && second == null && third == null;
    }

    private Node split(Node item) {
        if (item.getSize() < 3) return item;

        Node x = new Node(item.getKey(0), item.getFirst(), item.getSecond(), null, null, item.getParent()); // Создаем две новые вершины,
        Node y = new Node(item.getKey(2), item.getThird(), item.getFourth(), null, null, item.getParent());  // которые имеют такого же родителя, как и разделяющийся элемент.
        if (x.getFirst() != null)  x.getFirst().setParent(x);    // Правильно устанавливаем "родителя" "сыновей".
        if (x.getSecond() != null) x.getSecond().setParent(x);   // После разделения, "родителем" "сыновей" является "дедушка",
        if (y.getFirst() != null)  y.getFirst().setParent(y);    // Поэтому нужно правильно установить указатели.
        if (y.getSecond() != null) y.getSecond().setParent(y);

        if (item.getParent() != null) {
            item.getParent().insertToNode(item.getKey(1));

            if (item.getParent().getFirst() == item) item.getParent().setFirst(null);
            else if (item.getParent().getSecond() == item) item.getParent().setSecond(null);
            else if (item.getParent().getThird() == item) item.getParent().setThird(null);

            // Дальше происходит своеобразная сортировка ключей при разделении.
            if (item.getParent().getFirst() == null) {
                item.getParent().setFourth(item.getParent().getThird());
                item.getParent().setThird(item.getParent().getSecond());
                item.getParent().setSecond(y);
                item.getParent().setFirst(x);
            } else if (item.getParent().getSecond() == null) {
                item.getParent().setFourth(item.getParent().getThird());
                item.getParent().setThird(y);
                item.getParent().setSecond(x);
            } else {
                item.getParent().setFourth(y);
                item.getParent().setThird(x);
            }
            return item.getParent();
        } else {
            x.setParent(item);   // Так как в эту ветку попадает только корень,
            y.setParent(item);   // то мы "родителем" новых вершин делаем разделяющийся элемент.
            item.becomeNode2(item.getKey(1), x, y);
            return item;
        }
    }

    public Node insert(Node p, int k) { // Вставка ключа k
        if (p == null) return new Node(k); // Если дерево пусто, то создаем первую 2-3-вершину (корень)

        if (p.isLeaf()) p.insertToNode(k);
        else if (k <= p.getKey(0)) insert(p.getFirst(), k);
        else if ((p.getSize() == 1) || ((p.getSize() == 2) && k <= p.getKey(1))) insert(p.getSecond(), k);
        else insert(p.getThird(), k);

        return split(p);
    }

    private Node merge(Node leaf) {
        Node parent = leaf.getParent();

        if (parent.getFirst() == leaf) {
            parent.getSecond().insertToNode(parent.getKey(0));
            parent.getSecond().setThird(parent.getSecond().getSecond());
            parent.getSecond().setSecond(parent.getSecond().getFirst());

            if (leaf.getFirst() != null) parent.getSecond().setFirst(leaf.getFirst());
            else if (leaf.getSecond() != null) parent.getSecond().setFirst(leaf.getSecond());

            if (parent.getSecond().getFirst() != null) parent.getSecond().getFirst().setParent(parent.getSecond());

            parent.removeFromNode(parent.getKey(0));
            parent.setFirst(null);
        } else if (parent.getSecond() == leaf) {
            parent.getFirst().insertToNode(parent.getKey(0));

            if (leaf.getFirst() != null) parent.getFirst().setThird(leaf.getFirst());
            else if (leaf.getSecond() != null) parent.getFirst().setThird(leaf.getSecond());

            if (parent.getFirst().getThird() != null) parent.getFirst().getThird().setParent(parent.getFirst());

            parent.removeFromNode(parent.getKey(0));
            parent.setSecond(null);
        }

        if (parent.getParent() == null) {
            Node tmp = (parent.getFirst() != null) ? parent.getFirst() : parent.getSecond();
            tmp.setParent(null);
            return tmp;
        }
        return parent;
    }

    public Node search(Node p, int k) { // Поиск ключа k
        count++;
        if (p == null) return null;

        if (p.find(k)) return p;
        else if (k < p.getKey(0)) return search(p.getFirst(), k);
        else if ((p.getSize() == 2 && k < p.getKey(1)) || p.getSize() == 1) return search(p.getSecond(), k);
        else if (p.getSize() == 2) return search(p.getThird(), k);
        return null;
    }

    private Node redistribute(Node leaf) {
        Node parent = leaf.getParent();
        Node first = parent.getFirst();
        Node second = parent.getSecond();
        Node third = parent.getThird();

        if (parent.getSize() == 2 && first.getSize() < 2 && second.getSize() < 2 && third.getSize() < 2) {

            if (first.equals(leaf)) {
                // Случай, когда leaf является первым ребенком
                parent.setFirst(parent.getSecond());
                parent.setSecond(parent.getThird());
                parent.setThird(null);
                parent.getFirst().insertToNode(parent.getKey(0));
                parent.getFirst().setThird(parent.getFirst().getSecond());
                parent.getFirst().setSecond(parent.getFirst().getFirst());

                if (leaf.getFirst() != null) {parent.getFirst().setFirst(leaf.getFirst());}
                else if (leaf.getSecond() != null) {parent.getFirst().setFirst(leaf.getSecond());}

                if (parent.getFirst().getFirst() != null) parent.getFirst().getFirst().setParent(parent.getFirst());
                parent.removeFromNode(parent.getKey(0));

            } else if (second.equals(leaf)) {
                // Случай, когда leaf является вторым ребенком
                first.insertToNode(parent.getKey(0));
                parent.removeFromNode(parent.getKey(0));
                if (leaf.getFirst() != null) first.setThird(leaf.getFirst());
                else if (leaf.getSecond() != null) first.setThird(leaf.getSecond());

                if (first.getThird() != null) first.getThird().setParent(first);

                parent.setSecond(parent.getThird());
                parent.setThird(null);

            } else if (third.equals(leaf)) {
                // Случай, когда leaf является третьим ребенком
                second.insertToNode(parent.getKey(1));
                parent.setThird(null);
                parent.removeFromNode(parent.getKey(1));

                if (leaf.getFirst() != null) second.setThird(leaf.getFirst());
                else if (leaf.getSecond() != null) second.setThird(leaf.getSecond());

                if (second.getThird() != null) second.getThird().setParent(second);
            }

        } else if (parent.getSize() == 2 && (first.getSize() == 2 || second.getSize() == 2 || third.getSize() == 2)) {
            // Случаи, когда один из детей имеет 2 ключа
            if (third.equals(leaf)) {
                if (leaf.getFirst() != null) {
                    leaf.setSecond(leaf.getFirst());
                    leaf.setFirst(null);
                }

                leaf.insertToNode(parent.getKey(1));
                if (second.getSize() == 2) {
                    parent.setKey(1, second.getKey(1));
                    second.removeFromNode(second.getKey(1));
                    leaf.setFirst(second.getThird());
                    second.setThird(null);

                    if (leaf.getFirst() != null) leaf.getFirst().setParent(leaf);
                } else if (first.getSize() == 2) {
                    parent.setKey(1, second.getKey(0));
                    leaf.setFirst(second.getSecond());
                    second.setSecond(second.getFirst());

                    if (leaf.getFirst() != null) leaf.getFirst().setParent(leaf);

                    second.setKey(0, parent.getKey(0));
                    parent.setKey(0, first.getKey(1));
                    first.removeFromNode(first.getKey(1));
                    second.setFirst(first.getThird());

                    if (second.getFirst() != null) second.getFirst().setParent(second);
                    first.setThird(null);
                }
            } else if (second.equals(leaf)) {
                if (third.getSize() == 2) {
                    if (leaf.getFirst() == null) {
                        leaf.setFirst(leaf.getSecond());
                        leaf.setSecond(null);
                    }

                    second.insertToNode(parent.getKey(1));
                    parent.setKey(1, third.getKey(0));
                    third.removeFromNode(third.getKey(0));
                    second.setSecond(third.getFirst());

                    if (second.getSecond() != null) second.getSecond().setParent(second);
                    third.setFirst(third.getSecond());
                    third.setSecond(third.getThird());
                    third.setThird(null);

                } else if (first.getSize() == 2) {
                    if (leaf.getSecond() == null) {
                        leaf.setSecond(leaf.getFirst());
                        leaf.setFirst(null);
                    }

                    second.insertToNode(parent.getKey(0));
                    parent.setKey(0, first.getKey(1));
                    first.removeFromNode(first.getKey(1));
                    second.setFirst(first.getThird());

                    if (second.getFirst() != null) second.getFirst().setParent(second);
                    first.setThird(null);
                }
            } else if (first.equals(leaf)) {
                if (leaf.getFirst() == null) {
                    leaf.setFirst(leaf.getSecond());
                    leaf.setSecond(null);
                }

                first.insertToNode(parent.getKey(0));
                if (second.getSize() == 2) {
                    parent.setKey(0, second.getKey(0));
                    second.removeFromNode(second.getKey(0));
                    first.setSecond(second.getFirst());

                    if (first.getSecond() != null) first.getSecond().setParent(first);
                    second.setFirst(second.getSecond());
                    second.setSecond(second.getThird());
                    second.setThird(null);

                } else if (third.getSize() == 2) {
                    parent.setKey(0, second.getKey(0));
                    second.setKey(0, parent.getKey(1));
                    parent.setKey(1, third.getKey(0));
                    third.removeFromNode(third.getKey(0));
                    first.setSecond(second.getFirst());

                    if (first.getSecond() != null) first.getSecond().setParent(first);
                    second.setFirst(second.getSecond());
                    second.setSecond(third.getFirst());

                    if (second.getSecond() != null) second.getSecond().setParent(second);
                    third.setFirst(third.getSecond());
                    third.setSecond(third.getThird());
                    third.setThird(null);
                }
            }

        } else if (parent.getSize() == 1) {
            // Случай, когда у родителя только один ключ
            leaf.insertToNode(parent.getKey(0));

            if (first == leaf && second.getSize() == 2) {
                parent.setKey(0, second.getKey(0));
                second.removeFromNode(second.getKey(0));

                if (leaf.getFirst() == null) leaf.setFirst(leaf.getSecond());

                leaf.setSecond(second.getFirst());
                second.setFirst(second.getSecond());
                second.setSecond(second.getThird());
                second.setThird(null);
                if (leaf.getSecond() != null) leaf.getSecond().setParent(leaf);

            } else if (second == leaf && first.getSize() == 2) {
                parent.setKey(0, first.getKey(1));
                first.removeFromNode(first.getKey(1));

                if (leaf.getSecond() == null) leaf.setSecond(leaf.getFirst());

                leaf.setFirst(first.getThird());
                first.setThird(null);
                if (leaf.getFirst() != null) leaf.getFirst().setParent(leaf);
            }
        }
        return parent;
    }

    private Node fix(Node leaf) {
        if (leaf.getSize() == 0 && leaf.getParent() == null) { // Случай 0, когда удаляем единственный ключ в дереве
            return null;
        }
        if (leaf.getSize() != 0) { // Случай 1, когда вершина, в которой удалили ключ, имела два ключа
            if (leaf.getParent() != null) return fix(leaf.getParent());
            else return leaf;
        }

        Node parent = leaf.getParent();
        if (parent.getFirst().getSize() == 2 || parent.getSecond().getSize() == 2 || parent.getSize() == 2) {
            leaf = redistribute(leaf); // Случай 2, когда достаточно перераспределить ключи в дереве
        } else if (parent.getSize() == 2 && parent.getThird().getSize() == 2) {
            leaf = redistribute(leaf); // Аналогично
        } else {
            leaf = merge(leaf); // Случай 3, когда нужно произвести склеивание
        }

        return fix(leaf);
    }

    private Node searchMin(Node p) { // Поиск узла с минимальным элементом
        count++;
        if (p == null) return p;
        if (p.getFirst() == null) return p;
        else return searchMin(p.getFirst());
    }

    public Node remove(Node p, int k) { // Удаление ключа
        Node item = search(p, k); // Ищем узел, где находится ключ k

        if (item == null) return p;

        Node min = null;
        if (item.getKey(0) == k) min = searchMin(item.getSecond()); // Ищем эквивалентный ключ
        else min = searchMin(item.getThird());

        if (min != null) { // Меняем ключи местами
            int z = (k == item.getKey(0) ? item.getKey(0) : item.getKey(1));
            item.swap(z, min.getKey(0));
            item = min; // Перемещаем указатель на лист, т.к. min - всегда лист
        }

        item.removeFromNode(k); // И удаляем требуемый ключ из листа
        return fix(item); // Вызываем функцию для восстановления свойств дерева.
    }
}