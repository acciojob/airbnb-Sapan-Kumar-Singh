package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelManagementService {

    HotelManagementRepository hotelManagementRepository=new HotelManagementRepository();
    public String addHotel(Hotel hotel) {
        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
        Optional<Boolean>addOpt =hotelManagementRepository.addHotel(hotel);
        if(addOpt.isEmpty()){
            return " FAILURE";
        }

        return "SUCCESS";

    }

    public Integer addUser(User user)  {
        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        Optional<Integer> userOpt=hotelManagementRepository.addUser(user);

        return userOpt.get();
    }


    public String getHotelWithMostFacilities()  {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        Optional<String >facilityOpt=hotelManagementRepository. getHotelWithMostFacilities();

        return facilityOpt.get();

    }

    public int bookRoom(Booking booking) {

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

          Integer noOfRoomBook=booking.getNoOfRooms();
          String hotelName=booking.getHotelName();
          Hotel hotel=hotelManagementRepository.getHotel(hotelName);
          if(hotel.getAvailableRooms()<noOfRoomBook){
              return -1;
          }
        String bookingId= UUID.randomUUID().toString();
        while(hotelManagementRepository.notUnique(bookingId)){
            bookingId=UUID.randomUUID().toString();
        }
          Integer amountToBePaid=hotel.getPricePerNight()*noOfRoomBook;
         booking.setBookingId(bookingId);
         booking.setAmountToBePaid(amountToBePaid);
           hotelManagementRepository.updateRoom(hotelName,noOfRoomBook);
           hotelManagementRepository.bookRoom(booking);
           return amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        return hotelManagementRepository.getBookings(aadharCard);

    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel=hotelManagementRepository.getHotel(hotelName);
        List<Facility>oldFacility=hotel.getFacilities();
        HashSet<Facility> temp=new HashSet<>();
        temp.addAll(oldFacility);
        List<Facility>updateFacility=new ArrayList<>();
        updateFacility.addAll(oldFacility);
        for( Facility curr : newFacilities){
            if(!temp.contains(curr))
                updateFacility.add(curr);
        }
         hotel.setFacilities(updateFacility);
        hotelManagementRepository.updateFacility(hotelName,hotel);
        return hotel;
    }
}
