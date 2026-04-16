import java.util.Scanner;

public class HotelBooking {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean[] rooms = new boolean[5]; // 5 rooms
        int choice;

        do {
            System.out.println("\n--- HOTEL BOOKING ---");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Room");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Room " + (i + 1) + ": " + (rooms[i] ? "Booked You're WELCOME" : "Available"));
                  //  if (rooms[i] == true) {
                 //  print "Booked You're WELCOME";
                    //} else {
                      //print "Available";
                    //}
                }
            }

            else if (choice == 2) {
                System.out.print("Enter room number: ");
                int r = sc.nextInt();

                if (r >= 1 && r <= 5) {
                    if (!rooms[r - 1]) {
                        rooms[r - 1] = true;
                        System.out.println("Room booked!");
                    } else {
                        System.out.println("Already booked! SORRY");
                    }
                } else {
                    System.out.println("Invalid room!");
                }
            }

            else if (choice == 3) {
                System.out.print("Enter room number: ");
                int r = sc.nextInt();

                if (r >= 1 && r <= 5) {
                    if (rooms[r - 1]) {
                        rooms[r - 1] = false;
                        System.out.println("Booking cancelled!");
                    } else {
                        System.out.println("Already available!");
                    }
                } else {
                    System.out.println("Invalid room!");
                }
            }

            else if (choice == 4) {
                System.out.println("Thank you!");
            }

            else {
                System.out.println("Invalid choice!");
            }

        } while (choice != 4);

        sc.close();
    }
}
