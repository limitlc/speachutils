# speachutils

take the file of SpeechUtils.java to your application project's file
at the same time you should make sure the files in libs have already copied to libs in your project ;
then if you want a ui to show when app is using the SpeechUtils ,you  also need to copy iflytek into assets in your project;



if you are sure you have done all things writen above,you can use my SpeechUtils in your project with the command
"SpeechUtil.getInstance(Context context).setSpeechListener(SpeechUtil.OnSpeechListener listener).getSpeechContent();"
the listener is a callback of speechutils ,you can do something you want if you have a speechresult in this callback;
