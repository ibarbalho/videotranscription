package apitutorial;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class APITutorial {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		
		// POST
		
		Transcript transcript = new Transcript();
		transcript.setAudio_url("https://raw.githubusercontent.com/johnmarty3/JavaAPITutorial/main/Thirsty.mp4");
		
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(transcript);
		
		System.out.println(jsonRequest);
		
		HttpRequest postRequest = (HttpRequest) HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript"))
				.header("Authorization", Constants.API_KEY)
				.POST(BodyPublishers.ofString(jsonRequest))
				.build();
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
		
		System.out.println(postResponse.body());
		
		transcript = gson.fromJson(postResponse.body(), Transcript.class);
		
		System.out.println(transcript.getId());
		
		//Get
		
		HttpRequest getRequest = (HttpRequest) HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript/"+ transcript.getId()))
				.header("Authorization", Constants.API_KEY)
				.build();
		
		
		
		while(true) {
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			transcript = gson.fromJson(getResponse.body(), Transcript.class);
			
			System.out.println(transcript.getStatus());
			
			if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
				break;
			}
			
			Thread.sleep(1000);
		}
		
		System.out.println("Transcription completed!");
		System.out.println(transcript.getText());
	}
}
