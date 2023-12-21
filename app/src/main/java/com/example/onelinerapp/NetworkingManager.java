package com.example.onelinerapp;

import static android.system.Os.connect;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkingManager {

//    String apiKey = "f65d2999b20748f581e6ce70be2a1244";// not working
//    String apiKey = "53d0272ebf3843e8983269225a45f0b4";//not working apikey
//      String apiKey = "b9f7d4fde18a4b0aa5dadd8b47339eec";//working apikey
//    String apiKey = "dae424ceafef4b01a82a2841118dc47d";// working apikey
    String apiKey = "88cafb2c1323416b90ddf3ef75f5ac23";// working
//    String apiKey = "d40624f7007449d3a09ca4111663e824";//working

    String resMessage = "";
    interface NetworkingInterfaceListener{
        void networkingFinishWithJsonString(String json);
        void networkingFinishWithSuccess(boolean isSuccess);
    }
    NetworkingInterfaceListener listener;
    void getRandomJokes(){
        String urlString = "https://api.humorapi.com/jokes/random?api-key="+apiKey;
        connect(urlString);
    }
    void getSearchJokes(String query){
        String urlString = "https://api.humorapi.com/jokes/search?api-key="+apiKey+"&keywords="+query+"&number=10";
        connect(urlString);
    }
    void postAJoke(String joke){
//    https://api.humorapi.com/jokes?api-key=88cafb2c1323416b90ddf3ef75f5ac23
        String urlString = "https://api.humorapi.com/jokes?api-key="+apiKey;
        connectPost(urlString,joke);
    }

    private void connect(String urlString) {
        HttpURLConnection httpURLConnection = null;

        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL urlObj = new URL(urlString);
                    httpURLConnection  = (HttpURLConnection) urlObj.openConnection();
                    resMessage =  httpURLConnection.getResponseMessage();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    int v;
                    while ((v = inputStream.read()) != -1) {
                        buffer.append((char)v);
                    }
                    String jsonResponse = buffer.toString();
                    MyApp.mainhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("jsonResponse","jsonResponse "+jsonResponse);
                            listener.networkingFinishWithSuccess(true);
                            listener.networkingFinishWithJsonString(jsonResponse);
                            System.out.println("There is res message "+resMessage);

                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    listener.networkingFinishWithSuccess(false);
                    listener.networkingFinishWithJsonString(resMessage);
                    e.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }
            }
        });
    }

//    https://api.humorapi.com/jokes?api-key=88cafb2c1323416b90ddf3ef75f5ac23
      private void connectPost(String urlString,String postJoke) {
        HttpURLConnection httpURLConnection = null;
        MyApp.executorService.execute(new Runnable() {
           @Override
           public void run() {
              HttpURLConnection httpURLConnection = null;
               URL urlObj = null;
               try {
                   urlObj = new URL(urlString);
                   httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                   httpURLConnection.setDoOutput(true);

                   try(OutputStream os = httpURLConnection.getOutputStream()){
                    byte[] input = postJoke.getBytes(StandardCharsets.UTF_8);
                    os.write(input,0,input.length);
                   }
                   try(BufferedReader br = new BufferedReader(new
                           InputStreamReader(httpURLConnection.getInputStream(),"utf-8"))){
                       StringBuilder response = new StringBuilder();
                       String responseLine = null;
                       while((responseLine = br.readLine()) != null){
                           response.append(responseLine.trim());
                       }
                        MyApp.mainhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("post response","post response success");
                                listener.networkingFinishWithJsonString(String.valueOf(response));
                            }
                        });
                   }

               } catch (MalformedURLException e) {
                   throw new RuntimeException(e);
               } catch (ProtocolException e) {
                   throw new RuntimeException(e);
               } catch (IOException e) {
                   listener.networkingFinishWithJsonString("Post failure");
                   throw new RuntimeException(e);

               }

           }
           });
    }
//    void getRandomJokes(){
////    url: 'https://api-football-beta.p.rapidapi.com/teams',
//        String urlString = "https://api-football-beta.p.rapidapi.com/teams?league=39&season=2019";
//        connect(urlString);
//    }
//    private void connect(String urlString) {
//        HttpURLConnection httpURLConnection = null;
//        MyApp.executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection httpURLConnection = null;
//                try {
//                    URL urlObj = new URL(urlString);
//
//                    httpURLConnection  = (HttpURLConnection) urlObj.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.setRequestProperty("X-RapidAPI-Key","6782c9d67emsh4165da940a9b185p1795eejsndf0804adb6fb");
//                    httpURLConnection.setRequestProperty("X-RapidAPI-Host", "api-football-beta.p.rapidapi.com");
//
//                    InputStream inputStream = httpURLConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//                    int v;
//                    while ((v = inputStream.read()) != -1) {
//                        buffer.append((char)v);
//                    }
//                    String jsonResponse = buffer.toString();
//                    MyApp.mainhandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("jsonResponse","jsonResponse "+jsonResponse);
//                            listener.networkingFinishWithJsonString(jsonResponse);
//                        }
//                    });
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
////                    listener.networkingFinishWithJsonString("You have reached max number of daily Request");
//
//                    System.out.println("There is an error ");
//                    e.printStackTrace();
//                }
//                finally {
//                    httpURLConnection.disconnect();
//                }
//            }
//        });
//    }



}
