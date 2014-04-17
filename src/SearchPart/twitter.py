import sys
import codecs
from twython import Twython, TwythonError
tweets = []
username = sys.argv[1]

APP_KEY = "g1sBsjOkO6Ik2xQLWjPweg";
APP_SECRET = "RFADPSzcGEaPW3E5qQHxBiscydxrQkjqS2oPYgIIOzI";
OAUTH_TOKEN = "2359516250-RUL2dUtGx8PQzn3PPufVL5ZZZgB2yOEQAfg40AP";
OAUTH_TOKEN_SECRET ="v1WbJ44YQ5WYPPgXNDxe9P7E3HNtqdVKSjw1l7gMYIobJ";

user_timeline = []
twitter = Twython(APP_KEY, APP_SECRET, OAUTH_TOKEN, OAUTH_TOKEN_SECRET)
try:
    user_timeline = twitter.get_user_timeline(screen_name=username,count=200)
except TwythonError as e:
    print e
if user_timeline:
	for tweet in user_timeline:
		tweets.append(tweet['text'])

	f = codecs.open(username,'w',"UTF-8")

	for a in tweets:	
		f.write('<tweet>\n')
		f.write(a)
		f.write('\n</tweet>\n')
	f.close()
else:
	print "Invalid UserId......"
