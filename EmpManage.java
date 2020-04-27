import java.util.Scanner;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
class AppendableObjectOutputStream extends ObjectOutputStream
{
public AppendableObjectOutputStream(FileOutputStream fout) throws java.io.IOException
{
super(fout);
}
protected void writeStreamHeader() throws java.io.IOException{}//For Appending Objects
}
interface Designations //My Interface
{
void designations(); // Abstract Function
}
class Employee implements java.io.Serializable
{
String name;
int employeeId;
String department;
String designation;
void getEmployeeDetails()
{
Scanner sc = new Scanner(System.in);
System.out.print("Enter the name of the Employee: ");
name=sc.nextLine();
System.out.print("Enter Employee ID: ");
employeeId=sc.nextInt();
System.out.print("Department of the Employee: ");
sc.nextLine();
department = sc.nextLine();
}
void displayDetails()
{
System.out.println("Name of the Employee: "+name);
System.out.println("Employee ID: "+employeeId);
System.out.println("Employee designation: "+designation);
System.out.println("Department: "+department+"\n");
}
boolean compareId(int identity)
{
if(employeeId == identity)
return true;
else
return false;
}
}
class Temp extends Employee implements Designations,java.io.Serializable // Inherits Employee class and Implements Designations Interface
{
public void designations() // Abstract function of Inteface implemented in this class
{
System.out.println("No Proper Designations are available for temporary workers\n");
designation="Temporary Worker";
}
void getEmployeeDetails() //Function Overriding
{
super.getEmployeeDetails(); //Super keyword refers to parent class
designations();
}
}
class Perm extends Employee implements Designations,java.io.Serializable
{
public void designations()
{
System.out.println("Designations available are:\nClerk\nManager\nCOO\nCEO\n");
System.out.print("Enter Employee Designation: ");
Scanner sc = new Scanner(System.in);
designation=sc.next();
}
void getEmployeeDetails()
{
super.getEmployeeDetails();
designations();
}
}
class EmpManage
{
public static void main(String [] args) throws Exception
{
	String cont="y";
	while(cont.compareTo("y")==0)
	{
System.out.println("\t****************Employee Management System*****************\n");
System.out.println("Services Available: ");
System.out.println("1)Enter New Employee Details");
System.out.println("2)View Employee Details");
System.out.println("3)Update Employee Details");
System.out.println("4)Delete details of an Emmployee");

File f = new File("My_Project.txt");
int choice;
System.out.print("Enter your choice: ");
Scanner sc = new Scanner(System.in);
choice  = sc.nextInt();
boolean case_3=false;
switch(choice)
{
case 1:
{
System.out.println("Enter whether the Employee is Temporary or Permanent?(t/p)");
String emp;
emp = sc.next();
while(emp.compareTo("t")!=0&&emp.compareTo("p")!=0)
{
System.out.print("Invalid Employee type! Please Enter again");
emp=sc.next();
}
Employee ob1;
if(emp.compareTo("t")==0)
{
ob1 = new Temp();
ob1.getEmployeeDetails();
}
else
{
ob1 = new Perm();
ob1.getEmployeeDetails();
}
ObjectOutputStream out;
boolean new_file=false;
if(!f.exists())
{
f.createNewFile();
new_file=true;
}
FileOutputStream fout = new FileOutputStream(f,true);
if(new_file)
out = new ObjectOutputStream(fout);
else
out = new AppendableObjectOutputStream(fout);
out.writeObject(ob1);
out.close();
fout.close();
}
break;
case 2:
{
if(!f.exists())
System.out.println("Employee Database is Empty please Enter details of  Employees");
else
{
System.out.print("Enter Id of Employee: ");
int identity = sc.nextInt();
FileInputStream fin = new  FileInputStream(f);
ObjectInputStream in = new ObjectInputStream(fin);
Employee ob;
boolean n_fnd = false;
while(true)
{
try
{
ob = (Employee)in.readObject();
}
catch(Exception e)
{
n_fnd=true;
break;
}
if(ob.compareId(identity))
{
ob.displayDetails();
break;
}
}
if(n_fnd)
System.out.println("ID not found");
in.close();
fin.close();
}
}
break;
case 3:
 case_3=true;
case 4:
{
if(!case_3)
System.out.println("Enter the ID of the Employee to be Removed");
else
	System.out.println("Enter the ID of the Employee to be Updated");
int iden = sc.nextInt();
if(!f.exists()){System.out.println("No Employees  in Database\n");}
else
{
FileInputStream finp = new FileInputStream(f);
FileInputStream finp2 = new FileInputStream(f);
ObjectInputStream inp = new ObjectInputStream(finp);
Employee new_obj;
while(true)
{
try
{
new_obj = (Employee)inp.readObject();
}
catch(Exception e)
{
System.out.println("ID Not Found");
break;
}
if(new_obj.compareId(iden))
{
ObjectInputStream new_inp = new ObjectInputStream(finp2);
File n_f2 = new File("temp_file.txt");
if(n_f2.exists())
n_f2.delete();
n_f2.createNewFile();
ObjectOutputStream otp = new ObjectOutputStream(new FileOutputStream(n_f2));
new_obj = (Employee)new_inp.readObject();
boolean single_file=false;
if(!new_obj.compareId(iden))
otp.writeObject(new_obj);
else
{
try
{
new_obj = (Employee)new_inp.readObject();
otp.writeObject(new_obj);
}
catch(Exception e)
{
single_file=true;
}
}
if(!single_file)
{
otp.close();
otp = new ObjectOutputStream(new FileOutputStream(n_f2,true)){     //Anonymous Class
protected void writeStreamHeader() throws java.io.IOException{}//For Appending Objects
};

while(true)
{
try{new_obj = (Employee)new_inp.readObject();}
catch(Exception e){break;}
if(!new_obj.compareId(iden))
otp.writeObject(new_obj);
}
}
if(case_3)
{
String emp_stat;
System.out.print("Enter new Employment status of Employee "+iden+": ");
emp_stat=sc.next();
if(emp_stat.compareTo("t")==0)
	new_obj = new Temp();
else
	new_obj = new Perm();
System.out.println("Enter new Details of Employee");
new_obj.getEmployeeDetails();
otp.writeObject(new_obj);
System.out.println("Successfully Updated");
}
else
	System.out.println("Successfully Removed");
f.delete();
n_f2.renameTo(f);
break;
}
}
}
}
break;
default:
System.out.println("Invalid Option");
}
System.out.print("Do you wish to continue?(y/n): ");
cont = sc.next();
System.out.print("\n\n");
	}
System.out.println("Thank you. Please restart program to use the services");
}
}
