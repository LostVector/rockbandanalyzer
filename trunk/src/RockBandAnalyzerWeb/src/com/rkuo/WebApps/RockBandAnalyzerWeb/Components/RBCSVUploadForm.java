package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.PMF;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.RockBand.RockBandLocation;

import java.io.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import au.com.bytecode.opencsv.CSVReader;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 11, 2009
 * Time: 1:02:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class RBCSVUploadForm extends RBBaseUploadForm {

    public RBCSVUploadForm(String name) {
        super(name);
        return;
    }

    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    protected void onSubmit() {

        CSVReader csvReader;
        StringReader sr;
        String sCSV;
        String[] fields;

        sCSV = GetCSV();
        sr = new StringReader(sCSV);
        csvReader = new CSVReader(sr);

        // Read once to skip the headers
        try {
            fields = csvReader.readNext();
        }
        catch( IOException ioex ) {
            return;
        }

        while( true ) {

            try {
                fields = csvReader.readNext();
            }
            catch( IOException ioex ) {
                fields = null;
            }

            if( fields == null ) {
                break;
            }

            UpdateSong(fields);
        }

        return;
    }

    protected void UpdateSong(String[] fields) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;

        try {

            RockBandSong song;
            String midiTitle;
            Integer nValue;
            RockBandInstrumentDifficulty rbid;
            SimpleDateFormat sdf;
            Date dateReleased;
            Date datePublished;

            sdf = new SimpleDateFormat("m/d/yyyy");
            midiTitle = fields[0];

            query = pm.newQuery(RockBandSong.class);
            query.setFilter("generated.midiTitle == midiTitleParam");
            query.declareParameters("String midiTitleParam");
            query.setUnique(true);

            try {
                song = (RockBandSong)query.execute(midiTitle);
            }
            finally {
                query.closeAll();
            }

            if( song == null ) {
                throw new Exception();
            }

            song.getAssociated().setTitle(fields[1]);
            song.getAssociated().setArtist(fields[2]);
            song.getAssociated().setAlbum(fields[3]);
            song.getAssociated().setGenre(fields[4]);

            try {
                GregorianCalendar cal;

                dateReleased = sdf.parse(fields[5]);

                cal = new GregorianCalendar();
                cal.setTime(dateReleased);
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateReleased = cal.getTime();
            }
            catch( ParseException pex ) {
                return;
            }

//            song.getAssociated().setDateReleased( dateReleased );

            nValue = Integer.parseInt(fields[6]);
            song.getAssociated().setLocation(RockBandLocation.ToEnum(nValue));

            try {
                datePublished = sdf.parse(fields[7]);
            }
            catch( ParseException pex ) {
                return;
            }

//            song.getAssociated().setRBReleaseDate( datePublished );

            nValue = Integer.parseInt(fields[8]);
            rbid = RockBandInstrumentDifficulty.ToEnum(nValue);
            song.getAssociated().setBandDifficulty(rbid);
            nValue = Integer.parseInt(fields[9]);
            rbid = RockBandInstrumentDifficulty.ToEnum(nValue);
            song.getAssociated().setGuitarDifficulty(rbid);
            nValue = Integer.parseInt(fields[10]);
            rbid = RockBandInstrumentDifficulty.ToEnum(nValue);
            song.getAssociated().setDrumsDifficulty(rbid);
            nValue = Integer.parseInt(fields[11]);
            rbid = RockBandInstrumentDifficulty.ToEnum(nValue);
            song.getAssociated().setVocalsDifficulty(rbid);
            nValue = Integer.parseInt(fields[12]);
            rbid = RockBandInstrumentDifficulty.ToEnum(nValue);
            song.getAssociated().setBassDifficulty(rbid);

            song.getAssociated().setCover(Boolean.parseBoolean(fields[13]));
            song.getAssociated().setAvailableInRB1(Boolean.parseBoolean(fields[14]));
            song.getAssociated().setAvailableInRB2(Boolean.parseBoolean(fields[15]));

            pm.makePersistent(song);
        }
        catch( Exception ex ) {
        }
        finally {
            pm.close();
        }

        return;
    }

    protected String GetCSV() {

        FileUpload upload;
        InputStream fileIn;
        ByteArrayOutputStream baOut;

        baOut = new ByteArrayOutputStream();

        upload = fileUploadField.getFileUpload();
        if( upload == null ) {
            return null;
        }

        try {
            fileIn = upload.getInputStream();
        }
        catch( IOException ioex ) {
            return null;
        }

        try {
            fileIn.reset();
            for( int c = fileIn.read(); c != -1; c = fileIn.read() ) {
                baOut.write(c);
            }

            baOut.close();
        }
        catch( IOException ioex ) {
            return null;
        }

        return new String(baOut.toByteArray());
    }
}
