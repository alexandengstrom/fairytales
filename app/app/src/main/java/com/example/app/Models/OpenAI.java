package com.example.app.Models;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is reponsible for the communication with the OpenAI api.
 */
public class OpenAI {
    RequestQueue requestQueue;

    /**
     * Interface for the response callback.
     */
    public interface ResponseCallback {
        void onSuccess(Story story);
        void onError(VolleyError error);
    }

    /**
     * Empty constructor.
     */
    public OpenAI() {}

    /**
     * Receives data and sends a request.
     */
    public void requestStory(List<Character> characters, String location, String plot, String language, Context context, ResponseCallback callback) {
        String body = formatRequest(characters, location, plot, language);
        sendHTTPRequest(body, context, callback);
    }

    /**
     * Creates a formatted request based on the data provided.
     */
    private String formatRequest(List<Character> characters, String location, String plot, String language) {
        String output = "";

        output += "Language of the story: " + language + "\n";
        output += "Location of the story: " + location + "\n";
        output += "The user wishes that the story should be about: " + plot + "\n";
        output += "The following lines will contain information about the characters that should be included in the story:\n\n";

        for (int i = 0; i < characters.size(); i++) {
            int index = i + 1;
            output += "Character " + index + "\n";
            output += "Name: " + characters.get(i).getName() + "\n";
            output += "Age: " + characters.get(i).getAge() + "\n";
            output += "Occupation: " + characters.get(i).getOccupation() + "\n";
            output += "Additional information about this character: " + characters.get(i).getStory() + "\n\n";
        }

        return output;
    }

    /**
     * Sends a HTTP request using Volley.
     */
    private void sendHTTPRequest(String content, Context context, ResponseCallback callback) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://api.openai.com/v1/chat/completions";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        /**
                         * Called when response is received from the api, converts the response to JSON
                         * and if the conversion is done successfully the JSON will be converted to
                         * a Story object.
                         */
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            try {
                                String utf8String = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                jsonResponse = new JSONObject(utf8String);
                                String contentString = jsonResponse.getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content");

                                JSONObject contentJson = new JSONObject(contentString);

                                String title = contentJson.getString("title");
                                String shortDescription = contentJson.getString("shortDescription");
                                String content = contentJson.getString("content");

                                Story story = new Story(title, shortDescription, content);
                                StoriesRepository.save(story);
                                callback.onSuccess(story);
                            } catch (JSONException e) {

                                System.out.println(e);
                                throw new RuntimeException(e);
                            } catch (UnsupportedEncodingException e) {
                                System.out.println(e);
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        /**
                         * Called if the api respond with an error code.
                         */
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("POST Error: " + error);
                        }
                    }
            ) {
                /**
                 * Provides the headers for the HTTP request.
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + BuildConfig.API_KEY);
                    return params;
                }

                /**
                 * Provides the body for the HTTP request.
                 */
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return generateBody(content);
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
            };

        postRequest.setRetryPolicy(new RetryPolicy() {

            /**
             * Make the timeout much longer than standard since it will take some time to receive
             * the reponse.
             */
            @Override
            public int getCurrentTimeout() {
                return 5000000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

            queue.add(postRequest);
    }

    /**
     * Generate the body of the request and translate it to bytes.
     */
    private byte[] generateBody(String content) {
        String body = "";

        try {
            JSONObject object = new JSONObject();
            object.put("model", "gpt-4-1106-preview");
            JSONObject output_format = new JSONObject();
            output_format.put("type", "json_object");
            object.put("response_format", output_format);

            JSONArray messages = new JSONArray();

            JSONObject system = new JSONObject();
            system.put("role", "system");
            system.put("content", "Your job is to generate a bedtime-story for kids. The user will provide information about where the story should take place, what the story should be about and which characters should be included in the story. You can assume that the user already knows about the characters so you dont have to explain their individual backstories but you can use their individual stories when it fits the story. Your job is to return a bedtime story in JSON-format containing three keys. The first key is title and it should contain the title of the story. The next key is shortDescription and it should contain a short description of the plot, around 2-3 sentences. The last key is content it should contain the whole story. The bedtime story should be long and detailed, it should contain around 1000 words. The story should have some kind of twist or punchline in the end. It is okey for you to add more characters other than the ones the user has provided if you think it will make the story better. Remember that your reply should be only in JSON format, nothing more, nothing less. But you should still include newline characters etc in the content. You will also be told in which language you should write the story. This means the JSON values should be in this language, not the JSON keys");

            JSONObject user = new JSONObject();
            user.put("role", "user");
            user.put("content", content);

            messages.put(system);
            messages.put(user);

            object.put("messages", messages);

            body = object.toString();

        } catch(JSONException ex) {

        }
        return body.getBytes();
    }
}
