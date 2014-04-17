We normally spend a few moments of our daily lives on news articles to know about the things happening around!
We sometimes find information that interests us while may see huge amount of information that we never wanted to see!!
Just imagine if there was some sort of a system that would recommend you news based on your interests, well that is exactly what has been done as a part of this project to make everyone lives easier(We do know how busy our lives are!!!)

Let's have a look on the steps that have been followed to achieve the required results.
The Hindu news corpus has been used for this project. Basic techniques like stemming, stop word removal, named entity recognition have been applied and finally the keywords are indexed in a file for the query part.
The user has to input a twitter's user id, the system reads the latest 10 tweets of the user, analyses them and presents him the news that are best suited to his interests.

To use the system, you must have twython api, Jsoup api installed. Now all you need to do is run the GUI.java file and type in twitter user Id.
