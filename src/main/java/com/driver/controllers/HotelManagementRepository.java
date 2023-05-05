package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class HotelManagementRepository {

    private HashMap<String ,Hotel>hotelMap=new HashMap<>();
    private HashMap<Integer,User>userMap=new HashMap<>();

   private HashMap<String, Booking>bookingMap=new HashMap<>();

    public Optional<Boolean> addHotel(Hotel hotel) {
        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
        if(hotel.getHotelName().equals(null) ||hotel.equals(null) ){
            return Optional.empty();
        }
        if(this.hotelMap.containsKey(hotel.getHotelName())){
            return Optional.empty();
        }
        this.hotelMap.put(hotel.getHotelName(),hotel);
        return Optional.of(true);
    }

    public Optional<Integer> addUser(User user) {

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        this.userMap.put(user.getaadharCardNo(),user);
        return Optional.of(user.getaadharCardNo());
    }

    public Optional<String> getHotelWithMostFacilities() {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        ArrayList<String>store=new ArrayList<>();
        Integer numberOfFacilities=Integer.MIN_VALUE;
        for( String key : this.hotelMap.keySet()){
            if(this.hotelMap.get(key).getFacilities().size()>=numberOfFacilities){
                numberOfFacilities=this.hotelMap.get(key).getFacilities().size();
                store.add(this.hotelMap.get(key).getHotelName());
            }
        }
        if(store.size()==0){
            return Optional.of(null);
        }
        Collections.sort(store);

        return Optional.of(store.get(0));
    }

    public void bookRoom(Booking booking) {

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        this.bookingMap.put(booking.getBookingId(),booking);

    }

    public Hotel getHotel(String hotelName) {
        return this.hotelMap.get(hotelName);
    }

    public boolean notUnique(String bookingId) {
        if(this.bookingMap.containsKey(bookingId)){
            return true;
        }
        return false;
    }

    public void updateRoom(String hotelName,int noOfRoomBook) {

        Hotel temp=this.hotelMap.get(hotelName);
        temp.setAvailableRooms(temp.getAvailableRooms()-noOfRoomBook);
        this.hotelMap.put(hotelName,temp);

    }

    public int getBookings(Integer aadharCard) {

        // private HashMap<String ,Hotel>hotelMap=new HashMap<>();
        //    private HashMap<Integer,User>userMap=new HashMap<>();
        //
        //   private HashMap<String, Booking>bookingMap=new HashMap<>();
        //In this function return the bookings done by a person
        int cnt=0;
        for(String key : this.bookingMap.keySet()){
            if(this.bookingMap.get(key).getBookingAadharCard()==aadharCard)
                cnt++;
        }
        return cnt;
    }


    public void updateFacility(String hotelName, Hotel hotel) {
        this.hotelMap.put(hotelName,hotel);
    }
}
