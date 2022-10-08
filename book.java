import java.util.Date;

public class book implements  CheckOutAble
{
    String name;
    int itemValue = 0;
    int durationLimit = 0;
    int itemID = 0;
    Date dateCheckout;
    int copies = 1;
    boolean isRenew = false;
    private boolean isBestSeller;


    //Constructor for bestseller book
    public book(int itemID, String name, int value, int copies, boolean isBestSeller)
    {
        this.itemID = itemID;
        itemValue = value;
        this.name = name;
        this.isBestSeller = isBestSeller;
        this.copies = copies;

        //Check if bestseller == true
        if(this.isBestSeller)
            durationLimit = 14;//days
        else//not bestseller
            durationLimit = 21;//days
    }

    public void setBestSeller()
    {
        isBestSeller = true;
        durationLimit = 14;//days
    }

    public boolean isBestSeller()
    {
        return isBestSeller;
    }

    @Override
    public void setRenewStatus(boolean status) {
        isRenew = status;
    }

    @Override
    public boolean getRenewStats() {
        return isRenew;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getDateCheckout() {
        return dateCheckout;
    }

    @Override
    public int getDurationLimit() {
        return durationLimit;
    }

    @Override
    public int getID() {
        return itemID;
    }

    @Override
    public void setID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public int getCopies() {
        return copies;
    }

    @Override
    public void increaseCopy(int amount) {
        copies += amount;
    }

    @Override
    public void decreaseCopy(int amount) {
        if(amount >= copies)
        {
            copies = 0;
        }
        else
        {
            copies-=amount;
        }
    }
    @Override
    public int getValue() {
        return itemValue;
    }
    public void setDateCheckout(Date checkoutDate)
    {
        dateCheckout = checkoutDate;
    }
    public void setValue(int value){itemValue = value;}
}
