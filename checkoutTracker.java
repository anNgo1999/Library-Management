import java.awt.print.Book;
import java.io.*;
import java.net.IDN;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


public class checkoutTracker {
    BufferedReader br = null;
   
    int count = 0;
    //For checked out items
    HashMap<Integer, ArrayList<CheckOutAble>> checkoutLog = new HashMap<Integer, ArrayList<CheckOutAble>>();

    //For outstanding requests on an item
    ArrayList<CheckOutAble> outstandingRequestLog = new ArrayList<CheckOutAble>();
    


    //Constructor
    public checkoutTracker() {
        try {
            //read outstandingRequest.csv
            br = new BufferedReader(new FileReader("outstandingRequest.csv"));
            String line = "";
            while ((line = br.readLine()) != null) {
                int[] logLine = Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray();
                outstandingRequestLog.clear();
                for (int i : logLine) {
                    CheckOutAble itemToAdd = this.item(i);
                    if (itemToAdd != null)
                        outstandingRequestLog.add(this.item(i));
                }
            }

            //read checkoutLog.csv
            br = new BufferedReader(new FileReader("checkoutLog.csv"));
            int counter = 0;
            while((line = br.readLine()) != null)
            {
                String[] logLine = line.split(",");
                //Renew then Date Checkout then ItemID
                int id = Integer.parseInt(logLine[0]);
                ArrayList<CheckOutAble> items = new ArrayList<CheckOutAble>();
                if(logLine.length > 1)
                {
                    for(int i = 1; i < logLine.length; i++ )
                    {
                        boolean renewStat = Boolean.parseBoolean(logLine[i]);
                        i += 1;
                        Date checkoutDate = new SimpleDateFormat("MM/dd/yyyy").parse(logLine[i]);
                        i += 1;
                        CheckOutAble itemToAdd = this.item(Integer.parseInt(logLine[i]));
                        itemToAdd.setRenewStatus(renewStat);
                        itemToAdd.setDateCheckout(checkoutDate);
                        items.add(itemToAdd);
                    }
                    checkoutLog.put(id,items);
                }
            }
        } catch (Exception e){
            System.out.println("Checkout log: ");
            e.printStackTrace();
        }
    }

    
    public int checkOutItem(int userID, CheckOutAble item, boolean isChild)
    {
        // return 0 == checkout, 1 == outstanding request for item, 2 == no copies, 3 == a child and more than 5 items,
        if(isChild && checkoutLog.containsKey(userID))
        {
            if(checkoutLog.get(userID).size() == 5)
                return 3;
        }
        int returnValue = 0;
        if(checkOutStandingRequest(item.getID())){//There is an outstanding request for the item
            returnValue = 1;
        }
        else if(item.getCopies() < 1)//There are no copies
        {
            System.out.println("No copies");
            returnValue = 2;
        }
        else
        {
            returnValue = 0;
            if(checkoutLog.containsKey(userID))
            {
                Date currentDate = new Date(System.currentTimeMillis());
                item.setDateCheckout(currentDate);
                checkoutLog.get(userID).add(item);
            }
            else
            {
                ArrayList<CheckOutAble> itemList = new ArrayList<CheckOutAble>();
                Date currentDate = new Date(System.currentTimeMillis());
                item.setDateCheckout(currentDate);
                itemList.add(item);
                checkoutLog.put(userID,itemList);
            }
            writeData();
        }
        return returnValue;
    }

    //Renew an item
    public int renewItem(int itemID, int userID, librarySystem lib){

        //Check for outstanding request on item
        if (checkOutStandingRequest(itemID)) {
            //System.out.println("Item has outstanding request, cannot renew");
            return 2;
        }
        //Check for outstanding fine on user account
        if (lib.outStandingFine(userID) > 0.0) {
            //System.out.println("User has outstanding fines, cannot renew");
            return 3;
        }

        // get index of item to renew
        int itemToRenewIndex = -1;
        for (int i = 0; i < checkoutLog.get(userID).size(); i++) {
            if (checkoutLog.get(userID).get(i).getID() == itemID) {
                itemToRenewIndex = i;
                break;
            }
        }
        if(checkoutLog.get(userID).get(itemToRenewIndex).getRenewStats() == true)
        {
            return 1;
        }
        // If renewed item was found in user's checkout log, update and write new info to file
        if (itemToRenewIndex != -1) {
            try {
                //Format Date output to MM/dd/yyyy to write to file
                DateFormat writeFormat = new SimpleDateFormat("MM/dd/yyyy");
                DateFormat outPutFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

                Date currentDate = new Date(System.currentTimeMillis());
                checkoutLog.get(userID).get(itemToRenewIndex).setDateCheckout(currentDate);
                checkoutLog.get(userID).get(itemToRenewIndex).setRenewStatus(true);

                //Rewrite the data in checkoutLog.csv
                String writeToFileString = "";
                for (int id : checkoutLog.keySet()) {
                    writeToFileString += "" + id;
                    for (CheckOutAble itemToWrite : checkoutLog.get(id))
                    {
                        writeToFileString += "," + itemToWrite.getRenewStats();
                        writeToFileString += "," + writeFormat.format(outPutFormat.parse(itemToWrite.getDateCheckout().toString())) + ",";//date
                        writeToFileString += itemToWrite.getID();//item id
                    }
                    writeToFileString += "\n";
                }

                //Write to file
                BufferedWriter fileToWrite = new BufferedWriter(new FileWriter("checkoutLog.csv", false));
                fileToWrite.write(writeToFileString);
                fileToWrite.close();
            } catch (Exception e) {
                System.out.println("Checkoutlog write: \n" +e);

            }
        }
        return 0;
        

    }
    //Check for outstanding request
    public boolean checkOutStandingRequest(int itemID){ //Check if item have any outstanding request
        for (CheckOutAble item : outstandingRequestLog) {
            if (item.getID() == itemID) return true;
        }
        return false;
        }
    
    //Return an item
    public void returnItem(int itemID, int userID, catalog libCatalog){
        //Contains userID
        if (checkoutLog.containsKey(userID)) {
            //Initialize temp arraylist
            boolean removeItem = false;
            int itemToRemoveIndex = 0;
            //Iterate through checked out items
            for (CheckOutAble item : checkoutLog.get(userID)) {
               
                if (item.getID() == itemID) {
                    removeItem = true;
                    itemToRemoveIndex = checkoutLog.get(userID).indexOf(item);
                    break;
                }
            }

            //Remove item from record and write new info to file
            if(removeItem == true)
            {
                try
                {
                    //Decrease amount of copies
                    libCatalog.copiesIncrement(itemID);

                    //Remove item with the date from the Hashmap
                    checkoutLog.get(userID).remove(itemToRemoveIndex);

                    //Rewrite the data in checkoutLog.csv
                    writeData();
                }
                catch(Exception e)
                {
                    System.out.println("Checkoutlog write data: " +e);
                }
            }
        } else {
            System.out.println("Invalid user ID.");
        }
    }

     public ArrayList<CheckOutAble> outStandingFine (int userID, HashMap<Integer,Integer> itemList)//Get list of overdue items
     {
         ArrayList<CheckOutAble> overdueList = new ArrayList<>();
         if(checkoutLog.containsKey(userID))
         {
             //Get today date to compare
             try
             {
                 Date currentDate = new Date();

                 for(CheckOutAble item: checkoutLog.get(userID))
                 {
                     //Assign value to item
                     item.setValue(itemList.get(item.getID()));

                     // getting difference in time from both date classes
                     long difference_In_Time = currentDate.getTime() - item.getDateCheckout().getTime();

                     // getting difference in time to days (int)
                     long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time);

                     if(item instanceof book)// If item is a book
                     {
                         if(((book) item).isBestSeller())
                         {
                             if(difference_In_Days > 14)
                                 overdueList.add(item);
                         }
                         else
                         {
                             if(difference_In_Days > 21)
                                 overdueList.add(item);
                         }
                     }
                     else if(item instanceof video || item instanceof audio)// If item is a audio/video material
                         if(difference_In_Days > 14)
                             overdueList.add(item);
                 }

             }
             catch(Exception e)
             {
                 System.out.println("Overdue : \n"+e);
                 e.printStackTrace();
             }
         }
         return overdueList;
     }


     //Helper function to create item from itemID
    private CheckOutAble item(int itemID)
    {
        //Store in 2 arraylist: userIDList and passwordList
        try
        {
            String line = "";
            //Check item is in book
            BufferedReader fileReader = new BufferedReader(new FileReader("book.csv"));
            while((line = fileReader.readLine()) != null)
            {
                String[] itemLine = line.split(",");
                int id = Integer.parseInt(itemLine[0]);
                if(id == itemID)
                    return new book(id, itemLine[1], Integer.parseInt(itemLine[2]), Integer.parseInt(itemLine[3]),Boolean.parseBoolean(itemLine[4]));
            }
            fileReader.close();

            //Check item is in video
            fileReader = new BufferedReader(new FileReader("video.csv"));
            while((line = fileReader.readLine()) != null)
            {
                String[] itemLine = line.split(",");
                int id = Integer.parseInt(itemLine[0]);
                if(id == itemID)
                    return new video(id, itemLine[1], Integer.parseInt(itemLine[2]), Integer.parseInt(itemLine[3]) );
            }
            fileReader.close();

            //Check item is in audio
            fileReader = new BufferedReader(new FileReader("audio.csv"));
            while((line = fileReader.readLine()) != null)
            {
                String[] itemLine = line.split(",");
                int id = Integer.parseInt(itemLine[0]);
                if(id == itemID)
                    return new audio(id, itemLine[1], Integer.parseInt(itemLine[2]),Integer.parseInt(itemLine[3]) );
            }
            fileReader.close();
        }
        catch(Exception e)
        {
            System.out.println("Read file : \n" + e);
        }
        return null;
    }

    //Get user list of checkoutItem
    public ArrayList<CheckOutAble> getCheckoutItems(int userID)
    {
        return checkoutLog.get(userID);
    }

    //Helper function to write to file
    private void writeData()
    {
        try
        {
            //Format Date output to MM/dd/yyyy to write to file
            DateFormat writeFormat = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat outPutFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

            String writeToFileString = "";
            for(int id: checkoutLog.keySet())
            {
                writeToFileString += ""+id;
                for(CheckOutAble itemToWrite: checkoutLog.get(id))
                {
                    writeToFileString += "," + itemToWrite.getRenewStats();
                    writeToFileString += "," + writeFormat.format(outPutFormat.parse(itemToWrite.getDateCheckout().toString())) + ",";//date
                    writeToFileString += itemToWrite.getID();//item id
                }
                writeToFileString +="\n";
            }

            //Write to file
            BufferedWriter fileToWrite = new BufferedWriter(new FileWriter("checkoutLog.csv", false));
            fileToWrite.write(writeToFileString);
            fileToWrite.close();
            //System.out.println(writeToFileString);

        }
        catch(Exception e)
        {
            System.out.println("Write to file at end: \n"+ e);
        }
    }
   

  
}
