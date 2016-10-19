package com.okason.simplenotepad.utilities;

import com.okason.simplenotepad.models.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Valentine on 9/29/2015.
 */
public class SampleData {

    public static List<Note> getSampleNotes(){
        List<Note> sampleNotes = new ArrayList<Note>();


        //create the dummy note
        Note note1 = new Note();
        note1.setTitle("DisneyLand Trip");
        note1.setContent("We went to Disneyland today and the kids had lots of fun!");
        Calendar calendar1 = GregorianCalendar.getInstance();
        note1.setDateModified(calendar1.getTimeInMillis());

        //add note1 to the list
        sampleNotes.add(note1);


        //create the dummy note
        Note note2 = new Note();
        note2.setTitle("Gym Work Out");
        note2.setContent("I went to the Gym today and I got a lot of exercises");

        //change the date to random time
        Calendar calendar2 = GregorianCalendar.getInstance();
        calendar2.add(Calendar.DAY_OF_WEEK, 1);
        calendar2.add(Calendar.MILLISECOND, 10005623);
        note2.setDateModified(calendar2.getTimeInMillis());

        //add note2 to the list
        sampleNotes.add(note2);

        //create the dummy note
        Note note3 = new Note();
        note3.setTitle("Blog Post Idea");
        note3.setContent("I will like to write a blog post about how to make money online");

        //change the date to random time
        Calendar calendar3 = GregorianCalendar.getInstance();
        calendar3.add(Calendar.DAY_OF_WEEK, 2);
        calendar3.add(Calendar.MILLISECOND, 8962422);
        note3.setDateModified(calendar3.getTimeInMillis());

        //add note3 to the list
        sampleNotes.add(note3);


        //create the dummy note
        Note note4 = new Note();
        note4.setTitle("Cupcake Recipe");
        note4.setContent("Today I found a recipe to make cup cake from www.google.");

        //pad the date with random number of days and minute
        //so all the notes do not have the same time stamp
        Calendar calendar4 = GregorianCalendar.getInstance();
        calendar4.add(Calendar.DAY_OF_WEEK, 4);
        calendar4.add(Calendar.MILLISECOND, 49762311);
        note4.setDateModified(calendar4.getTimeInMillis());

        //add note4 to the list
        sampleNotes.add(note4);


        //create the dummy note
        Note note5 = new Note();
        note5.setTitle("Notes from Networking Event");
        note5.setContent("Today I attended a developer's networking event and it was great");

        //pad the date with two days
        //pad the date with random number of days and minute
        //so all the notes do not have the same time stamp
        Calendar calendar5 = GregorianCalendar.getInstance();
        calendar5.add(Calendar.MILLISECOND, 2351689);
        note5.setDateModified(calendar5.getTimeInMillis());

        //add note5 to the list
        sampleNotes.add(note5);

        return sampleNotes;
    }

}
