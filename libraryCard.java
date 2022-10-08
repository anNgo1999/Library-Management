class libraryCard{


    private int userID;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private boolean isChild;
    private double fine;
  
    public libraryCard(int id, String firstName, String lastName, String address,String phoneNumber, int age)
    {
        userID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;

        if(age <= 12)
            isChild = true;
        else
            isChild = false;
    }

    public libraryCard(int id, String firstName, String lastName, String address,String phoneNumber, Boolean isChild)
    {
        userID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isChild = isChild;
    }

    public String getFirstName(){
        return firstName;
      }
  
    public String getLastName(){
        return lastName;
    }
  
    public String  getAddress(){
        return address;
      }
  
    public int  getID(){
        return userID;
      }
  
    public String  getPhoneNumber(){
        return phoneNumber;
      }
  
    public double  getFine(){
        return fine;
      }
  
    public boolean isChild(){
        return isChild;
      }
  
  
    public void setName(String newName){
        this.firstName = newName;
    }
    public void setLastName(String newLastName){
        this.lastName = newLastName;
    }
    public void setAddress(String newAddress){
      this.address = newAddress;
    }
  
    public void setPhoneNumber(String newPhoneNumber){
        this.phoneNumber = newPhoneNumber;
      }
  
    public void setFine(int newFine){
        this.fine = newFine;
    }
  
    public void addFine(int updateFine){
        this.fine += updateFine;
    }

  
  
  }