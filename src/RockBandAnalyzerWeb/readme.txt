All Rock Band, Rock Band 2, and AC/DC master tracks- http://www.fretsonfire.net/forums/viewtopic.php?t=30926
All DLC from 12/23/2008 onward - http://www.fretsonfire.net/forums/viewtopic.php?f=17&t=36616
Some raw midi files - http://www.echoingthesound.org/phpbbx/viewtopic.php?t=29040
Search for Xusder at ETS Forums
Guitar icons - http://www.iconspedia.com/pack/stratocaster-guitars-2017/
music icons - http://www.iconspedia.com/cat/music/
Real rock band icons
- Vinyl And Gold Instrument Icons! - http://rockband.scorehero.com/forum/viewtopic.php?t=13208
- HiRes Instrument Icons *The Official Ones* - http://rockband.scorehero.com/forum/viewtopic.php?t=12714
- Platinum icons - http://rockband.scorehero.com/forum/viewtopic.php?t=13149
- star icons - http://rockband.scorehero.com/forum/viewtopic.php?t=6787

Notes on adding images to wicket - http://javathoughts.capesugarbird.com/2007/09/wicket-and-accessibility.html

- Dynamically populate the admin page with tasks to retrigger
- add some retry logic for scraping ... I see failures occasionally that must be due to network conditions
-- Detaching objects with embedded classes doesn't work .. or I don't understand how it's supposed to work
- Load recent songs ... max 10 on the front page, but show count of songs uploaded in the past 30 days and link
to a page with those songs
- Create a page that explicitly shows any dotcomsong that does not have a raw .mid equivalent uploaded yet.
- Version number
- admin interface to clear sessions
- Flesh out Song details page
-- add images to spruce up song details
- add on the fly ajax search
- Do I really need to scrape rockband.com every day?
- graph songs by duration
- graph/display songs by gold star score as a percentage of max score

- possible to do a keys only query in java?
- Fix sorting of songs that start with parentheses
- Subsort by alpha on various song browse options
- implement data overrides for bad data on rockband.com (bad release dates falling on 2008/02/01 and "no part" songs)
- Songs with glitches ... sort of have this, but I am detecting way too many glitched songs. May not be doing this right.
- Graph note density per measure
- Filter for songs without certain instrumental tracks
-- yyz has no vocals
-- release dates for a lot of early content are all on feb 1, 2008
- Calculate percentage of notes that are bass on each song
- Implement a way to wipe sessions
- add career manager
- is black sabbath NIB really corrupt?
- Other instruments
- Page to display songs with bad stats or information
- memcache or build pages behind the scenes
- memcache missing songs
+ figure out why scraping perfect drug exceptioned
- Implement song metadata editing.
- develop a flash rockband chart display program
- Estimate max score from big rock endings.
- scrape rockband.com for score tracking.
- analyze distribution of leaderboard scores for each song to determine difficulty, popularity, etc.
- No squeeze paths (some people, like myself, could give a fuck all about squeezing)
- Average max score vs baseline score curve and this song's curve
Other difficulties
- Add optimal paths for RB1
- distribution of song genres over time
- distribution of difficulties over time
- detect miss paths
- detect overdrive continuation points and path off them.
- Guitar pathing
-- It may be possible to partition OD at certain intervals and exit early from traversing certain paths
because the range in question clearly has less notes
-- a full, but simplified, scan involves getting as much OD as possible, scanning all opportunities for activation where
each opportunity is defined as a OD covering a specific range of notes.  Practically speaking, we can identify uniquely
discrete activations by testing OD activations that start on a specific chord as well as end on a specific chord.
We can use the same algorithm as drums ... if we ever have the same amount of OD as another path, but a lower score,
then we are on a non optimal path.
+ add midi title in place of song title where data unavailable.
+ Fix the upload form to use the same logic as the servlet
+ Implement gvis stuff.
+ Add theme to site.
+ current distribution of song genres
+ current distribution of difficulties
+ Add the source code, about, and terms of use pages.
+ Songs where the optimal path requires breakneck speed
+ Songs where the optimal path requires normal speed
+ Songs with solos
+ Songs with big rock endings
+ Songs that cannot be gold starred.
+ Implement basic sorting on the browse page.
+ fix the alignment of the account login links.
+ Add flags indicating whether songs are available in RB1 or RB2
+ Add optimal paths for RB2, RB2 Breakneck
+ Create denormalized rb structure for easy stat gathering
+ Separate cron exe from analyzer exe
+ Most notes
+ Highest possible score
+ Duration ... longest to shortest
+ implement a scan of Rock Band .com to look for songs I don't have yet
+ tried cobra ... fails on app engine due to missing java.net.proxy support
+ tried htmlcleaner ... doesn't generate valid xml
+ jtidy works ... now I can't parse my xml b/c google app engine doesn't support xpath
+ Write a page to scan existing songs for missing metadata
+ scrape implemented, but song list scrape is timing out
+ clean up existing song metadata (in progress)
+ deploy new version (getting tehre)
+ Songs that are covers
+ batch drop tables
+ batch song detail scrape
+ Fix song sorting to group numbers and punctuation
+ Fix weird URL'ization of pages.  Apparently caused by my prototype ajax completion search box. Need to work on this.
+ Add stats for current distributions of genres, difficulties, etc
+ Add advanced sorting options
+ Secure the web services
+ implement a sample annotated timeline chart
+ look at the character encoding for blue oyster cult (needed to set UTF-8 encoding in tidy)
+ modify saving midiTitles to strip trailing "1" character (this lets us line up with rockband.com identifiers)
+ Fix default date of new songs (set them to 0 ms)
+ Handle batch timeouts better ... one by one updates, order things so that I don't double write, etc.
+ implement normal/breakneck spreads in normal/breakneck optimal sort view
+ Recently uploaded/released songs
+ Add admin interface to regenerate data.
+ Fix reprocessing on the live site
+ Filter out songs with 0ms dates
+ implement a properties cache in the datastore
++ add number of missing songs to the cache
+ X of Y songs loaded
+ Test manual uploads and forced rescanning (works gud)
+ Implement "No Part" as a category of difficulty
++ Sort of done, but apparently rockband.com doesn't implement this correctly so I'm scraping bad data
+ Update site stats more frequently, or determine when to trigger a stats update. Am now triggering a recount when scraping finishes.
+ Stats last updated on x date.
+ Clean up the sidebar
+ Guard against files without beat tracks (frets on fire edited mid's, for example) This actually is done, I just wasn't logging it that well.
+ secure cron doesn't work ... let app engine team know (they know)
