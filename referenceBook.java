public class referenceBook implements UnCheckoutAble
{
    private int id;
    private String name;
    private int value;

    public referenceBook(int id, String name, int value)
    {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return value;
    }
}


