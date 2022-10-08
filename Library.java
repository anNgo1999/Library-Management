import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Library
{
    private final ArrayList<Integer> userIDList = new ArrayList<>();
    private final ArrayList<String> passwordList = new ArrayList<>();
    //Fine currentLogin user own
    String fines = "0";

    //Initialize helper function getInfoActionListener
    getInfoActionListener getInfoAction = new getInfoActionListener();

    //currentUser
    int currentUserID = 0;
    //Initialize Library System
    librarySystem system = new librarySystem();

    //Helper to show item info
    public class getInfoActionListener implements ActionListener
    {
        private JList<String> list;
        private int modeID = 0;

        private HashMap<Integer,CheckOutAble> itemLookupMap;
        public void setList(JList<String> list)
        {
            this.list = list;
        }

        public void mode(int id)
        {
            //0 == checkoutItem, 0 == CheckoutItem, 1 == returnItem, 2 == requestItem, 3 == renewItem
            modeID = id;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(list != null)
            {
                //Get item ID
                String itemID = list.getSelectedValue();
                itemID = itemID.substring(0,5);

                //Initialize base components
                JFrame tempFrame = new JFrame();
                JPanel tempPanel = new JPanel();

                //Get info depend on mode
                if(modeID == 0)//------------------------------------------------------CheckoutItem
                {
                    CheckOutAble item = null;
                    for(CheckOutAble checkoutItem: system.getItemList())//Get item info from the catalog
                    {
                        if(checkoutItem.getID() == Integer.parseInt(itemID))
                        {
                            item = checkoutItem;
                            break;
                        }
                    }

                    //GUI--------------------------------------------------------------------

                    //Components
                    tempPanel.setLayout(new GridLayout(5,1));
                    Font font = new Font("Arial", Font.BOLD, 25);

                    JLabel idLabel, nameLabel, bestSellerLabel, valueLabel, copiesLabel;
                    idLabel = new JLabel("ID:        " + item.getID());
                    idLabel.setFont(font);
                    nameLabel = new JLabel("Name:        " + item.getName());
                    nameLabel.setFont(font);
                    copiesLabel = new JLabel("Copies:        " + item.getCopies());
                    copiesLabel.setFont(font);
                    valueLabel = new JLabel("Value:        $" + item.getValue());
                    valueLabel.setFont(font);

                    //Add to frame and panel
                    tempPanel.add(idLabel);
                    tempPanel.add(nameLabel);
                    //For book
                    if(item instanceof  book )
                    {
                        bestSellerLabel = new JLabel();
                        bestSellerLabel.setFont(font);
                        if(itemID.charAt(4) != '0')
                        {
                            bestSellerLabel.setText("Best Seller:        TRUE");
                        }
                        else
                        {
                            bestSellerLabel.setText("Best Seller:        FALSE");
                            tempPanel.add(bestSellerLabel);

                        }
                        tempPanel.add(bestSellerLabel);
                    }
                    tempPanel.add(valueLabel);
                    tempPanel.add(copiesLabel);

                    //GUI--------------------------------------------------------------------
                }
                else if(modeID == 1)//------------------------------------------------------returnItem
                {

                    CheckOutAble item = itemLookupMap.get(Integer.parseInt(itemID));
                    //Get due date
                    Date dueDate = new Date(item.getDateCheckout().getTime() + TimeUnit.DAYS.toMillis(item.getDurationLimit()));//Due date

                    //GUI--------------------------------------------------------------------

                    //Components
                    tempPanel.setLayout(new GridLayout(5,1));
                    Font font = new Font("Arial", Font.BOLD, 25);

                    JLabel idLabel, nameLabel, bestSellerLabel, checkoutLabel, dueDateLabel;
                    idLabel = new JLabel("ID:        " + item.getID());
                    idLabel.setFont(font);
                    nameLabel = new JLabel("Name:        " + item.getName());
                    nameLabel.setFont(font);
                    checkoutLabel = new JLabel("Date Checkout:        " + item.getDateCheckout());
                    checkoutLabel.setFont(font);
                    dueDateLabel = new JLabel("Due Date:        " + dueDate);
                    dueDateLabel.setFont(font);

                    //Add to frame and panel
                    tempPanel.add(idLabel);
                    tempPanel.add(nameLabel);

                    tempPanel.add(checkoutLabel);
                    tempPanel.add(dueDateLabel);

                    //For book
                    if(item instanceof  book )
                    {
                        bestSellerLabel = new JLabel();
                        bestSellerLabel.setFont(font);
                        if(itemID.charAt(4) != '0')
                        {
                            bestSellerLabel.setText("Best Seller:        TRUE");
                        }
                        else
                        {
                            bestSellerLabel.setText("Best Seller:        FALSE");
                            tempPanel.add(bestSellerLabel);

                        }
                        tempPanel.add(bestSellerLabel);
                    }
                }
                else if(modeID == 2)
                {
                    //Get item's info
                    CheckOutAble item = null;
                    for(CheckOutAble tempItem: system.getItemList())//Get item info from the catalog
                    {
                        if(tempItem.getID() == Integer.parseInt(itemID))
                        {
                            item = tempItem;
                            break;
                        }
                    }
                    //GUI--------------------------------------------------------------------

                    //Components
                    tempPanel.setLayout(new GridLayout(5,1));
                    Font font = new Font("Arial", Font.BOLD, 25);

                    JLabel idLabel, nameLabel, bestSellerLabel, itemType, duration, value;
                    idLabel = new JLabel("ID:        " + item.getID());
                    idLabel.setFont(font);
                    nameLabel = new JLabel("Name:        " + item.getName());
                    nameLabel.setFont(font);

                    //Add to frame and panel
                    tempPanel.add(idLabel);
                    tempPanel.add(nameLabel);

                    //Check item type
                    itemType = new JLabel();
                    itemType.setFont(font);
                    tempPanel.add(itemType);
                    if(item instanceof  book )
                    {
                        itemType.setText("Category:      Book");
                        bestSellerLabel = new JLabel();
                        bestSellerLabel.setFont(font);
                        //Check if best seller
                        if(itemID.charAt(4) != '0')
                        {
                            bestSellerLabel.setText("Best Seller:        TRUE");
                        }
                        else
                        {
                            bestSellerLabel.setText("Best Seller:        FALSE");
                            tempPanel.add(bestSellerLabel);

                        }
                        tempPanel.add(bestSellerLabel);
                    }
                    else if(item instanceof  video)
                        itemType.setText("Category:      Video");
                    else
                        itemType.setText("Category:      Audio");

                    duration = new JLabel("Days can checkout:     " + item.getDurationLimit() + " Days");
                    duration.setFont(font);

                    value = new JLabel("Value:       $" + item.getValue());
                    value.setFont(font);

                    tempPanel.add(duration);
                    tempPanel.add(value);
                }
                else if(modeID == 3)
                {
                    CheckOutAble item = itemLookupMap.get(Integer.parseInt(itemID));
                    //Get due date
                    Date dueDate = new Date(item.getDateCheckout().getTime() + TimeUnit.DAYS.toMillis(item.getDurationLimit()));//Due date

                    //GUI--------------------------------------------------------------------

                    //Components
                    tempPanel.setLayout(new GridLayout(5,1));
                    Font font = new Font("Arial", Font.BOLD, 25);

                    JLabel idLabel, nameLabel, bestSellerLabel, checkoutLabel, dueDateLabel;
                    idLabel = new JLabel("ID:        " + item.getID());
                    idLabel.setFont(font);
                    nameLabel = new JLabel("Name:        " + item.getName());
                    nameLabel.setFont(font);
                    checkoutLabel = new JLabel("Date Checkout:        " + item.getDateCheckout());
                    checkoutLabel.setFont(font);
                    dueDateLabel = new JLabel("Due Date:        " + dueDate);
                    dueDateLabel.setFont(font);

                    //Add to frame and panel
                    tempPanel.add(idLabel);
                    tempPanel.add(nameLabel);

                    tempPanel.add(checkoutLabel);
                    tempPanel.add(dueDateLabel);

                    //For book
                    if(item instanceof  book )
                    {
                        bestSellerLabel = new JLabel();
                        bestSellerLabel.setFont(font);
                        if(itemID.charAt(4) != '0')
                        {
                            bestSellerLabel.setText("Best Seller:        TRUE");
                        }
                        else
                        {
                            bestSellerLabel.setText("Best Seller:        FALSE");
                            tempPanel.add(bestSellerLabel);

                        }
                        tempPanel.add(bestSellerLabel);
                    }
                }


                //Add panel into frame
                tempFrame.add(tempPanel);
                tempFrame.setSize(600,400);

                tempFrame.setVisible(true);
                tempFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }

        }

        public void setLookUpMap(HashMap<Integer,CheckOutAble> itemLookupMap) {
            this.itemLookupMap = itemLookupMap;
        }
    }
    public Library()
    {
        //Parse data for log in and sign up
        inforParse();

        //GUI START--------------------------------------------------------------------------------------

        //Initialize layout
        GridLayout gridLayout = new GridLayout(3,1);
        CardLayout cardLayout = new CardLayout();//For multiple menus

        //Initialize components
        JFrame mainFrame = new JFrame("Main Menu");
        JPanel mainPanel = new JPanel();
        JPanel mainMenuPanel = new JPanel();
        mainPanel.setLayout(cardLayout);
        mainMenuPanel.setLayout(gridLayout);

        JLabel welcomeLabel = new JLabel("Welcome to the Library", SwingConstants.CENTER);
        JButton login = new JButton("Log In");
        JButton signUp = new JButton("Sign Up");

        //Set layout
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 52));
        login.setFont(new Font("Times New Roman", Font.BOLD, 42));
        signUp.setFont(new Font("Times New Roman", Font.BOLD, 42));

        //Add components
        mainMenuPanel.add(welcomeLabel);
        mainMenuPanel.add(login);
        mainMenuPanel.add(signUp);

        //Add panels into cardlayout
        mainPanel.add(mainMenuPanel, "Main");//Add main menu to card layout with name "Main"
        mainFrame.add(mainPanel);

        //Set mainFrame visible and normal operations
        mainFrame.setVisible(true);
        mainFrame.setSize(700,600);
        mainFrame.setLocationRelativeTo(null);//Set location of window to middle of screen
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Set GUI to show main menu GUI first
        cardLayout.show(mainPanel, "Main");

        //GUI END--------------------------------------------------------------------------------------



        //Set buttons function
        login.addActionListener(new ActionListener() { //LOG IN
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.add(loginMenuGUI(mainFrame, mainPanel, cardLayout), "Log In");
                mainFrame.setSize(600,200);
                mainFrame.setTitle("Log In");
                cardLayout.show(mainPanel,"Log In");
            }
        });

        signUp.addActionListener(new ActionListener() { //SIGN UP
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.add(signUpMenuGUI(mainFrame, mainPanel, cardLayout), "Sign Up");
                mainFrame.setTitle("Sign Up");
                mainFrame.setSize(400,500);
                cardLayout.show(mainPanel, "Sign Up");
            }
        });


    }

    private JPanel loginMenuGUI(JFrame mainFrame, JPanel mainPanel, CardLayout cardLayout)
    {
        //GUI START--------------------------------------------------------------------------------------

        GridLayout loginLayout = new GridLayout(3,2);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(loginLayout);// Set layout for panel

        JLabel userIDLabel = new JLabel("User ID: ", SwingConstants.CENTER);
        JTextField userID = new JTextField("");
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        JTextField password = new JTextField("");
        JButton login = new JButton("Log In:");
        JButton backToMain = new JButton("Back to Main Menu");

        loginPanel.add(userIDLabel);
        loginPanel.add(userID);
        loginPanel.add(passwordLabel);
        loginPanel.add(password);
        loginPanel.add(login);
        loginPanel.add(backToMain);


        //GUI END--------------------------------------------------------------------------------------

        //Set function for buttons
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    if(isNumber(userID.getText())) {
                        int id = Integer.parseInt(userID.getText());
                        //validate id
                        if(userIDList.contains(id))
                        {
                            //validate password
                            String pass = password.getText();
                            if(passwordList.contains(pass))//Everything pass validation
                            {
                                //LOG IN SUCCESSFUL++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                                //Set current user
                                currentUserID = Integer.parseInt(userID.getText());
                                system.setCurrentUser(currentUserID);
                                fines = ""+system.outStandingFine(currentUserID);

                                //Clear text fields
                                userID.setText("");
                                password.setText("");

                                //Set main frame size and set menu to User Menu
                                mainPanel.add(userMenuGUI(mainFrame, mainPanel, cardLayout), "User");
                                mainFrame.setTitle("User Menu");
                                mainFrame.setSize(700,600);
                                cardLayout.show(mainPanel, "User");
                                //LOG IN SUCCESSFUL++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                            }
                            else//wrong password
                            {
                                JOptionPane.showMessageDialog(mainFrame, "Invalid Password\nPlease try again");
                            }
                        }
                        else//ID does not exist
                        {
                            JOptionPane.showMessageDialog(mainFrame, "ID does not exist\nPlease try again");
                        }

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(mainFrame, "No characters should be in user ID\n");
                    }

                }
                catch(Exception ex)
                {
                    System.out.println("Login : " +ex);
                }
            }
        });
        backToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setTitle("Main Menu");
                mainFrame.setSize(700,600);
                cardLayout.show(mainPanel, "Main");
            }
        });

        return loginPanel;
    }

    private JPanel signUpMenuGUI(JFrame mainFrame, JPanel mainPanel, CardLayout cardLayout)
    {
        //GUI START--------------------------------------------------------------------------------------

        GridLayout signUpLayout = new GridLayout(7,2);

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(signUpLayout);//Set layout for panel

        JLabel fNameLabel = new JLabel("First Name: ", SwingConstants.CENTER);
        JTextField fName = new JTextField("");

        JLabel lNameLabel = new JLabel("Last Name:", SwingConstants.CENTER);
        JTextField lName = new JTextField("");

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        JTextField password = new JTextField("");

        JLabel addressLabel = new JLabel("Address:", SwingConstants.CENTER);
        JTextField address = new JTextField("");

        JLabel phoneLabel = new JLabel("Phone Number:", SwingConstants.CENTER);
        JTextField phoneNum = new JTextField("");

        JLabel ageLabel = new JLabel("Age:", SwingConstants.CENTER);
        JTextField age = new JTextField("");

        JButton signUp = new JButton("Sign Up");
        JButton backToMain = new JButton("Back to Main Menu");

        signUpPanel.add(fNameLabel);
        signUpPanel.add(fName);
        signUpPanel.add(lNameLabel);
        signUpPanel.add(lName);
        signUpPanel.add(passwordLabel);
        signUpPanel.add(password);
        signUpPanel.add(addressLabel);
        signUpPanel.add(address);
        signUpPanel.add(phoneLabel);
        signUpPanel.add(phoneNum);
        signUpPanel.add(ageLabel);
        signUpPanel.add(age);
        signUpPanel.add(signUp);
        signUpPanel.add(backToMain);

        //GUI END--------------------------------------------------------------------------------------

        //Set function for buttons
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Generate ID
                boolean sameId = true;
                int id = (int)(Math.random()*10000000);
                //Check if id already exist
                while(sameId)
                {
                    if(userIDList.contains(id))
                        id = (int)(Math.random()*10000000);
                    else
                        sameId = false;
                }

                //Check if password exist
                if(passwordList.contains(password.getText()))
                    JOptionPane.showMessageDialog(mainPanel, "Password already exist");
                else // Generate a new user and add into login.csv and user.csv files
                {
                    try
                    {
                        //Get values to save
                        String firstName = fName.getText();
                        String lastName = lName.getText();
                        String passWd = password.getText();
                        String phoneNumber = phoneNum.getText();
                        String addressString = address.getText();
                        int userAge = Integer.parseInt(age.getText());

                        //add new user to userIDList and passwordList
                        userIDList.add(id);
                        passwordList.add(passWd);

                        //Write to login.csv
                        BufferedWriter fileToWrite = new BufferedWriter(new FileWriter("login.csv", true));
                        fileToWrite.write("\n"+id +","+password.getText());
                        fileToWrite.close();

                        //Write to login.csv
                        system.signUp(id, firstName, lastName, passWd, addressString, phoneNumber, userAge);

                        //set current user
                        currentUserID = id;
                        system.setCurrentUser(currentUserID);

                        //LOG IN SUCCESSFUL++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        //Clear text fields
                        fName.setText("");
                        lName.setText("");
                        address.setText("");
                        phoneNum.setText("");
                        age.setText("");

                        //Set size and swap to User Menu
                        mainPanel.add(userMenuGUI(mainFrame, mainPanel, cardLayout), "User");
                        mainFrame.setTitle("User Menu");
                        mainFrame.setSize(700,600);
                        cardLayout.show(mainPanel, "User");

                        //LOG IN SUCCESSFUL++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    }
                    catch(Exception ex)
                    {
                        System.out.println("longin 2" + ex);
                    }
                }
            }
        });
        backToMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setTitle("Main Menu");
                mainFrame.setSize(700,600);
                cardLayout.show(mainPanel, "Main");
            }
        });

        return signUpPanel;
    }

    private JPanel userMenuGUI(JFrame mainFrame, JPanel mainPanel, CardLayout cardLayout)
    {
        //GUI START--------------------------------------------------------------------------------------

        GridLayout userLayout = new GridLayout(6,3);
        //Initialize new components
        JPanel userPanel = new JPanel();
        userPanel.setLayout(userLayout);//Set layout for panel

        //Functions
        JLabel welcomeLabel = new JLabel("<html>Welcome Back <br>" + currentUserID +"</html> ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("", Font.BOLD, 25));
        JButton requestRenew = new JButton("Request Renew");
        JButton checkoutItem = new JButton("Checkout Item");
        JButton returnItem = new JButton("Return Item");
        JButton requestItem = new JButton("Request Item");
        JButton showFinesDetail = new JButton("Fines Details");
        JButton showUnCheckoutAbleItem = new JButton("<html>Show Magazine and <br>Reference Book</html>");
        JButton getUserInfo = new JButton("User Info");
        JButton LogOut = new JButton("Log Out");
        JLabel finesOwn = new JLabel("Outstanding Fine: $"+fines, SwingConstants.CENTER);

        //Add new components
        userPanel.add(new JLabel());//Blank component for padding
        userPanel.add(welcomeLabel);
        userPanel.add(new JLabel());//Blank component for padding
        userPanel.add(requestRenew);
        userPanel.add(new JLabel());//Blank component for padding
        userPanel.add(checkoutItem);
        userPanel.add(returnItem);
        userPanel.add(new JLabel());//Blank component for padding
        userPanel.add(requestItem);
        userPanel.add(showFinesDetail);
        userPanel.add(showUnCheckoutAbleItem);
        userPanel.add(getUserInfo);
        userPanel.add(new JLabel());
        userPanel.add(LogOut);
        userPanel.add(new JLabel());
        userPanel.add(new JLabel());//Blank component for padding
        userPanel.add(finesOwn);

        //GUI END--------------------------------------------------------------------------------------

        //Set function for buttons

        //Show magazine and reference book
        //DONE AND WORK
        showUnCheckoutAbleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame uncheckoutFrame = new JFrame("Checkout Item");
                JPanel uncheckoutPanel = new JPanel();

                DefaultListModel<String> listModel = new DefaultListModel<>();

                //Add checkout items into the list
                for(UnCheckoutAble item: system.getUncheckoutAble())
                {
                    String itemString = item.getId()+"";
                    if(item instanceof  magazine)
                        itemString += "     Magazine     "+ item.getName();
                    else if(item instanceof  referenceBook)
                        itemString += "     Reference Book    "+ item.getName();

                    listModel.addElement(itemString);//Add item to list
                }
                JList list = new JList(listModel);
                list.setFont(new Font("",Font.BOLD, 15));
                JScrollPane listScrollPane = new JScrollPane(list);

                //Add list into scroll pane
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                list.setSelectedIndex(0);
                list.setVisibleRowCount(20);
                list.setFixedCellWidth(500);

                //Add scroll pane to checkoutItemPanel
                uncheckoutPanel.setPreferredSize(new Dimension(500,500));
                uncheckoutPanel.add(listScrollPane);

                uncheckoutFrame.add(uncheckoutPanel);
                uncheckoutFrame.setSize(800,800);
                uncheckoutFrame.setVisible(true);
                uncheckoutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            }
        });

        //Show amount of fines user own for each item pass due date
        showFinesDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame detailFineFrame = new JFrame("");
                JPanel detailFinePanel = new JPanel();
                detailFinePanel.setPreferredSize(new Dimension(500,500));

                JEditorPane finesDisplayArea = new JEditorPane();
                finesDisplayArea.setEditable(false);
                finesDisplayArea.setContentType("text/html");

                detailFinePanel.add(finesDisplayArea);
                detailFineFrame.add(detailFinePanel);

                detailFineFrame.setVisible(true);
                detailFineFrame.pack();
                detailFineFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                HashMap<Integer, ArrayList<String>> finesDetailList = system.showFinesDetail(currentUserID);
                String displayString = "Fines Detail";
                for(int id: finesDetailList.keySet())
                {
                    //name , value, daysOverdue, subTotal
                    displayString += "<pre>\n Item ID: " + id;
                    displayString += "\n      Item Name: " + finesDetailList.get(id).get(0);
                    displayString += "\n      Item Value: $" + finesDetailList.get(id).get(1);
                    displayString += "\n      Days Overdue: " + finesDetailList.get(id).get(2);
                    displayString += "\n      <b> Fines Amount:</b> $"+finesDetailList.get(id).get(3) + "\n</pre>";
                }
                finesDisplayArea.setText(displayString);
            }
        });
        //Request Renew-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        //DONE AND WORK
        requestRenew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //GUI START--------------------------------------------------------------------------------------------

                //List of checkout items
                ArrayList<CheckOutAble> itemList = system.getCheckoutItems(currentUserID);
                if(itemList != null)
                {
                    JFrame renewFrame = new JFrame("Request Item Frame");
                    JPanel renewPanel = new JPanel();
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

                    DefaultListModel<String> listModel = new DefaultListModel<>();
                    JList<String> list = new JList<>(listModel);
                    JScrollPane listScrollPane = new JScrollPane(list);

                    //Add checkout items into the list
                    for(CheckOutAble item: itemList)
                    {
                        String itemString = item.getID() + " " +item.getName();
                        listModel.addElement(itemString);
                    }
                    //Button
                    JButton getInfoButton = new JButton("Get info");
                    JButton renewItemButton = new JButton("Renew Item");

                    buttonPanel.add(getInfoButton);
                    buttonPanel.add(renewItemButton);
                    buttonPanel.add(Box.createHorizontalStrut(5));
                    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    //Add list into scroll pane
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    list.setSelectedIndex(0);
                    list.setVisibleRowCount(5);

                    //Add scroll pane to checkoutItemPanel
                    renewPanel.add(listScrollPane, BorderLayout.CENTER);
                    renewPanel.add(buttonPanel, BorderLayout.PAGE_END);

                    renewFrame.add(renewPanel);
                    renewFrame.pack();
                    renewFrame.setVisible(true);
                    renewFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                    //GUI END----------------------------------------------------------------------------------

                    //Button function
                    //Fine Detail
                    //Renew-----------------------------------------------------------=============================
                    renewItemButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String itemID = list.getSelectedValue();
                            itemID = itemID.substring(0,5);
                            switch(system.renewItem(Integer.parseInt(itemID)))
                            {
                                case 0: JOptionPane.showMessageDialog(renewFrame, "Item Successfully Renew");
                                        break;
                                case 1: JOptionPane.showMessageDialog(renewFrame, "You already renew this item");
                                        break;
                                case 2: JOptionPane.showMessageDialog(renewFrame, "There is an outstanding request for this item");
                                        break;
                                case 3: JOptionPane.showMessageDialog(renewFrame, "There is a fines on your account");
                                        break;
                            }
                        }
                    });
                    HashMap<Integer, CheckOutAble> itemLookupMap = new HashMap<Integer,CheckOutAble>();//List of checkoutItem
                    for (CheckOutAble item : itemList)
                    {
                        itemLookupMap.put(item.getID(), item);
                    }
                    getInfoAction.setList(list);
                    getInfoAction.setLookUpMap(itemLookupMap);
                    getInfoAction.mode(3);
                    getInfoButton.addActionListener(getInfoAction);
                }
                else
                {
                    JOptionPane.showMessageDialog(userPanel, "No checkout items in account to renew");
                }
                //Renew-----------------------------------------------------------=============================
            }
        });
        //Request Renew-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        //Return Item -_-_-_-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_-_-_-_-
        //DONE AND WORK
        returnItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(system.getCheckoutItems(currentUserID) != null)
                {
                    ArrayList<CheckOutAble> itemList = system.getCheckoutItems(currentUserID);
                    HashMap<Integer, CheckOutAble> itemLookupMap = new HashMap<Integer,CheckOutAble>();//List of checkoutItem
                    for (CheckOutAble item : itemList)
                    {
                        itemLookupMap.put(item.getID(), item);
                    }

                    //GUI-------------------------------------------------------------------------------------------------
                    JFrame returnItemFrame = new JFrame("Return Item Frame");
                    JPanel returnItemPanel = new JPanel();
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

                    DefaultListModel<String> listModel = new DefaultListModel<>();
                    JList list = new JList(listModel);
                    JScrollPane listScrollPane = new JScrollPane(list);

                    //Add checkout items into the list
                    for(CheckOutAble item: itemList)
                    {
                        String itemString = item.getID() + " " +item.getName();
                        listModel.addElement(itemString);
                    }
                    //Buttons
                    JButton getInfoButton = new JButton("Get Info");
                    JButton returnItemButton = new JButton("Return Item");

                    buttonPanel.add(getInfoButton);
                    buttonPanel.add(returnItemButton);
                    buttonPanel.add(Box.createHorizontalStrut(5));
                    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    //Add list into scroll pane
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    list.setSelectedIndex(0);
                    list.setVisibleRowCount(5);

                    //Add scroll pane to checkoutItemPanel
                    returnItemPanel.add(listScrollPane, BorderLayout.CENTER);
                    returnItemPanel.add(buttonPanel, BorderLayout.PAGE_END);

                    returnItemFrame.add(returnItemPanel);
                    returnItemFrame.pack();
                    returnItemFrame.setVisible(true);
                    returnItemFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                    //GUI-------------------------------------------------------------------------------------------------


                    //Get info of item
                    getInfoAction.mode(1);
                    getInfoAction.setList(list);
                    getInfoAction.setLookUpMap(itemLookupMap);
                    getInfoButton.addActionListener(getInfoAction);

                    //Return Item -_-_-_-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_-_-_-_--_-_-_-_-_-_-_-_-_-_-_-_-_-

                    returnItemButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //Get item ID
                            String itemID = list.getSelectedValue().toString();
                            itemID = itemID.substring(0,5);
                            listModel.remove(list.getSelectedIndex());
                            list.repaint();
                            list.revalidate();

                            //Return item
                            system.returnItem(Integer.parseInt(itemID), currentUserID);
                        }
                    });
                }
                else
                {
                    JOptionPane.showMessageDialog(userPanel, "No checkout items in account");
                }
            }
        });

        //Checkout Item================================================================================
        //DONE AND WORK
        checkoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //GUI------------------------------------------------------------------------------------------

                ArrayList <CheckOutAble> itemList = system.getItemList();

                JFrame checkoutFrame = new JFrame("Checkout Item");
                JPanel checkoutPanel = new JPanel();
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

                DefaultListModel<String> listModel = new DefaultListModel<>();

                //Add checkout items into the list
                for(CheckOutAble item: itemList)
                {
                    String itemString = item.getID()+"";
                    if(item instanceof  book)
                        itemString += "     Book     "+ item.getName();
                    else if(item instanceof  video)
                        itemString += "     Video    "+ item.getName();
                    else
                        itemString += "     Audio    "+ item.getName();

                    listModel.addElement(itemString);//Add item to list
                }


                JList list = new JList(listModel);
               list.setFont(new Font("",Font.BOLD, 15));
                JScrollPane listScrollPane = new JScrollPane(list);


                //Buttons
                JButton getInfoButton = new JButton("Get Info");
                JButton checkOutItemButton = new JButton("Checkout Item");

                buttonPanel.add(getInfoButton);
                buttonPanel.add(checkOutItemButton);
                buttonPanel.add(Box.createHorizontalStrut(20));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                //Add list into scroll pane
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                list.setSelectedIndex(0);
                list.setVisibleRowCount(20);
                list.setFixedCellWidth(500);

                //Add scroll pane to checkoutItemPanel
                checkoutPanel.setPreferredSize(new Dimension(500,500));
                checkoutPanel.add(listScrollPane);
                checkoutPanel.add(buttonPanel);

                checkoutFrame.add(checkoutPanel);
                checkoutFrame.setSize(800,800);
                checkoutFrame.setVisible(true);
                checkoutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                //GUI------------------------------------------------------------------------------------------

                checkOutItemButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        String itemID = list.getSelectedValue().toString();
                        itemID = itemID.substring(0,5);
                        if(Double.parseDouble(fines) > 0)
                        {
                            JOptionPane.showMessageDialog(checkoutFrame, "You must pay the outstanding fine before you can check out an item");
                        }
                        else
                        {
                            // return 0 == checkout, 1 == outstanding request for item, 2 == no copies
                            int successValue = system.checkOutItem(Integer.parseInt(itemID), currentUserID);
                            if(successValue == 1)
                            {
                                JOptionPane.showMessageDialog(checkoutFrame, "There are outstanding request for the item");
                            }
                            else if(successValue == 2)
                            {
                                JOptionPane.showMessageDialog(checkoutFrame, "No copies for checkout");
                            }
                            else if(successValue == 3)
                            {
                                JOptionPane.showMessageDialog(checkoutFrame, "Children 12 and under can only checkout 5 items");
                            }
                            else if(successValue == 0)
                            {
                                JOptionPane.showMessageDialog(checkoutFrame, "Item Checkout");
                            }
                        }
                    }
                });
                //Get item's info function
                getInfoAction.mode(0);
                getInfoAction.setList(list);
                getInfoButton.addActionListener(getInfoAction);
            }
        });
        //Checkout Item================================================================================

        //Request Item||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
        //DONE AND WORK
        requestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<CheckOutAble> requestAbleList = new ArrayList<>();
                for(CheckOutAble item: system.getItemList())//Get item with no copies
                {
                    if(item.getCopies() == 0)
                        requestAbleList.add(item);//Add into list
                }

                if(!requestAbleList.isEmpty())
                {
                    //GUI------------------------------------------------------------------------------------------
                    JFrame requestItemFrame = new JFrame("Request Item");
                    JPanel requestItemPanel = new JPanel();
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

                    DefaultListModel<String> listModel = new DefaultListModel<>();

                    //Add checkout items into the list
                    for(CheckOutAble item: requestAbleList)
                    {
                        String itemString = item.getID()+"";
                        if(item instanceof  book)
                            itemString += "     Book     "+ item.getName();
                        else if(item instanceof  video)
                            itemString += "     Video    "+ item.getName();
                        else
                            itemString += "     Audio    "+ item.getName();

                        listModel.addElement(itemString);//Add item to list
                    }
                    JList list = new JList(listModel);
                    list.setFont(new Font("",Font.BOLD, 15));
                    JScrollPane listScrollPane = new JScrollPane(list);


                    //Buttons
                    JButton getInfoButton = new JButton("Get Info");
                    JButton requestItemButton = new JButton("Request Item");

                    buttonPanel.add(getInfoButton);
                    buttonPanel.add(requestItemButton);
                    buttonPanel.add(Box.createHorizontalStrut(20));
                    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                    //Add list into scroll pane
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    list.setSelectedIndex(0);
                    list.setVisibleRowCount(20);
                    list.setFixedCellWidth(500);

                    //Add scroll pane to checkoutItemPanel
                    requestItemPanel.setPreferredSize(new Dimension(500,500));
                    requestItemPanel.add(listScrollPane);
                    requestItemPanel.add(buttonPanel);

                    requestItemFrame.add(requestItemPanel);
                    requestItemFrame.setSize(800,800);
                    requestItemFrame.setVisible(true);
                    requestItemFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                    //GUI------------------------------------------------------------------------------------------

                    //Buttons function
                    requestItemButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String itemID = list.getSelectedValue().toString();
                            itemID = itemID.substring(0,5);
                            boolean requestSuccess = system.requestItem(Integer.parseInt(itemID));
                            if(requestSuccess)
                                JOptionPane.showMessageDialog(requestItemFrame, "Item request successful");
                            else
                                JOptionPane.showMessageDialog(requestItemFrame, "Copies of item available for checkout");
                        }
                    });

                    //Get info
                    getInfoAction.mode(2);
                    getInfoAction.setList(list);
                    getInfoButton.addActionListener(getInfoAction);
                }
            }
        });

        //Get user information
        //DONE AND WORK
        getUserInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GridLayout infoLayout =  new GridLayout(4,2);

                JFrame subFrame = new JFrame(system.getCurrentUser().getLastName() +" Info");
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(infoLayout);

                //Components
                JLabel firstName, lastName, phoneNumberLabel, isChildLabel, idLabel;
                idLabel = new JLabel("ID Number: "+ system.getCurrentUser().getID()+"");
                firstName = new JLabel( "First Name: " + system.getCurrentUser().getFirstName());
                lastName = new JLabel("Last Name: " + system.getCurrentUser().getLastName());
                JTextArea addressLabel = new JTextArea();
                phoneNumberLabel = new JLabel("Phone Number: " + system.getCurrentUser().getPhoneNumber());
                isChildLabel = new JLabel("Child: " + system.getCurrentUser().isChild()+"");


                //Configure jtextarea
                addressLabel.setWrapStyleWord(true);
                addressLabel.setLineWrap(true);
                addressLabel.setOpaque(false);
                addressLabel.setEditable(false);
                addressLabel.setFocusable(false);
                addressLabel.setText("Address: " +system.getCurrentUser().getAddress().substring(1,system.getCurrentUser().getAddress().length()-1));
                addressLabel.setFont(new Font("", Font.BOLD, 12));

                infoPanel.add(idLabel);
                infoPanel.add(phoneNumberLabel);
                infoPanel.add(firstName);
                infoPanel.add(lastName);
                infoPanel.add(addressLabel);
                infoPanel.add(isChildLabel);

                subFrame.add(infoPanel);
                subFrame.setVisible(true);
                subFrame.setSize(300,400);
                subFrame.setLocationRelativeTo(null);//Set location of window to middle of screen
                subFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            }
        });

        //Log out of account
        //DONE AND WORK
        LogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setTitle("Main Menu");
                mainFrame.setSize(700,600);
                cardLayout.show(mainPanel, "Main");
            }
        });

        return userPanel;
    }

    //Add userID and password for log in and sign up
    private void inforParse()
    {
        try
        {
            String line;
            BufferedReader fileReader = new BufferedReader(new FileReader("login.csv"));
            while((line = fileReader.readLine()) != null)
            {
                String[] user = line.split(",");
                userIDList.add(Integer.parseInt(user[0]));
                passwordList.add(user[1]);
            }
            fileReader.close();
        }
        catch (IOException ex)
        {
            System.out.println("Reading data: \n"+ex);
        }
    }

    private boolean isNumber(String value)
    {
        return value.matches("-?\\d+(\\.\\d+)?");
    }



    public static void main(String[] args)
    {
        Library library = new Library();
    }
}