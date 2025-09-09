package user;
import java.util.*;

// ================= Room Base Class ==================
class Room {
    protected int roomNo;
    protected String type;
    protected double basePrice;
    protected boolean isBooked;
    protected String customerName;

    public Room(int roomNo, String type, double basePrice) {
        this.roomNo = roomNo;
        this.type = type;
        this.basePrice = basePrice;
        this.isBooked = false;
        this.customerName = "";
    }

    public boolean checkAvailability() {
        return !isBooked;
    }

    public void book(String customerName) {
        if (!isBooked) {
            this.isBooked = true;
            this.customerName = customerName;
            System.out.println("Room " + roomNo + " booked for " + customerName);
        } else {
            System.out.println("Room " + roomNo + " is already booked!");
        }
    }

    public void book(String customerName, boolean includeBreakfast) {
        book(customerName);
        if (includeBreakfast) {
            System.out.println("Breakfast included for " + customerName);
        }
    }

    public void cancel() {
        if (isBooked) {
            System.out.println("Booking for " + customerName + " cancelled in Room " + roomNo);
            isBooked = false;
            customerName = "";
        } else {
            System.out.println("Room " + roomNo + " is not booked.");
        }
    }

    public double computeBill(int days) {
        return basePrice * days;
    }

    public int getRoomNo() { return roomNo; }
    public String getType() { return type; }
    public double getBasePrice() { return basePrice; }
    public boolean getStatus() { return isBooked; }
    public String getCustomerName() { return customerName; }
}

// ================= Deluxe Room ==================
class DeluxeRoom extends Room {
    private boolean miniBar;
    private boolean seaView;

    public DeluxeRoom(int roomNo, double basePrice, boolean miniBar, boolean seaView) {
        super(roomNo, "Deluxe", basePrice);
        this.miniBar = miniBar;
        this.seaView = seaView;
    }

    @Override
    public double computeBill(int days) {
        double extra = 0;
        if (miniBar) extra += 500;
        if (seaView) extra += 1000;
        return (basePrice + extra) * days;
    }
}

// ================= Suite Room ==================
class SuiteRoom extends Room {
    private boolean jacuzzi;
    private boolean privateButler;

    public SuiteRoom(int roomNo, double basePrice, boolean jacuzzi, boolean privateButler) {
        super(roomNo, "Suite", basePrice);
        this.jacuzzi = jacuzzi;
        this.privateButler = privateButler;
    }

    @Override
    public double computeBill(int days) {
        double luxuryFee = 2000;
        if (jacuzzi) luxuryFee += 500;
        if (privateButler) luxuryFee += 1500;
        return (basePrice + luxuryFee) * days;
    }
}

// ================= Booking Service ==================
class BookingService {
    private List<Room> rooms;
    private double totalRevenue;

    public BookingService() {
        this.rooms = new ArrayList<>();
        this.totalRevenue = 0;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void showAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (r.checkAvailability()) {
                System.out.println("Room No: " + r.getRoomNo() + " | Type: " + r.getType() + " | Price: " + r.getBasePrice());
            }
        }
    }

    public void bookRoom(int roomNo, String customerName, int days) {
        for (Room r : rooms) {
            if (r.getRoomNo() == roomNo) {
                if (r.checkAvailability()) {
                    r.book(customerName);
                    double bill = r.computeBill(days);
                    totalRevenue += bill;
                    System.out.println("Invoice: Customer " + customerName + " | Amount: " + bill);
                } else {
                    System.out.println("Room " + roomNo + " is not available.");
                }
                return;
            }
        }
        System.out.println("Room not found!");
    }

    public void cancelRoom(int roomNo) {
        for (Room r : rooms) {
            if (r.getRoomNo() == roomNo) {
                r.cancel();
                return;
            }
        }
        System.out.println("Room not found!");
    }

    public void dailyReport() {
        int occupied = 0;
        for (Room r : rooms) {
            if (r.getStatus()) occupied++;
        }
        System.out.println("\n--- Daily Report ---");
        System.out.println("Occupied Rooms: " + occupied);
        System.out.println("Available Rooms: " + (rooms.size() - occupied));
        System.out.println("Total Revenue: " + totalRevenue);
    }
}

// ================= Main Class ==================
public class HotelAppMain {
    public static void main(String[] args) {
        BookingService bookingService = new BookingService();

        // Adding Rooms
        bookingService.addRoom(new Room(101, "Standard", 2000));
        bookingService.addRoom(new DeluxeRoom(201, 3500, true, false));
        bookingService.addRoom(new DeluxeRoom(202, 4000, true, true));
        bookingService.addRoom(new SuiteRoom(301, 6000, true, true));

        // Show Available Rooms
        bookingService.showAvailableRooms();

        // Book Rooms
        bookingService.bookRoom(201, "Alice", 2);
        bookingService.bookRoom(301, "Bob", 3);

        // Attempt duplicate booking
        bookingService.bookRoom(201, "Charlie", 1);

        // Cancel a booking
        bookingService.cancelRoom(201);

        // Generate Daily Report
        bookingService.dailyReport();

        // Show Available Rooms after changes
        bookingService.showAvailableRooms();
    }
}
