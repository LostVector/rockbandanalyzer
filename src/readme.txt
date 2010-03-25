Include instructions on placing jdk in /lib.
Include instructions on placing appengine-java-sdk in /lib.
Include instructions on adding jdk/bin to path so that appengine can run properly.
Include script or debug instructions for running the app server locally.
Include only the lib and lib/user directories.  Not sure what the lib/shared and lib/tools dirs are for, but
 including them made my debug webserver take 3 minutes to load a new page.
Copy                                                                        
RockBandAnalyzerExe - A command line executable wrapping the functionality of RockBandAnalyzerLib.
RockBandAnalyzerLib - The core library containing functionality for parsing MIDI's, analyzing the
resulting Rock Band data, etc.
RockBandAnalyzerWar - A deprecated web application wrappin the functionality of RockBandAnalyzerLib.
Uses Java Server Faces 1.1 and Google App Engine.  Determined that JSF was inferior to Wicket, so
I switched.
RockBandAnalyzerWeb - The current web application which wraps the functionality of RockBandAnalyzerLib.
Uses Apache Wicket 1.4rc2 and the Google App Engine for Java 1.2.0 SDK.
