import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== AI Resume Analyzer =====");
        System.out.println("Enter Resume Text:");

        String resume = scanner.nextLine();

        String apiKey = "PASTE_API_KEY_HERE";

        String prompt = "Analyze this resume and give strengths, missing skills, and suggestions: " + resume;

        String jsonInput = """
        {
          "contents": [{
            "parts":[{"text":"%s"}]
          }]
        }
        """.formatted(prompt);

        URL url = new URL(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                        + apiKey);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {

            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        InputStream stream;

        if (conn.getResponseCode() >= 400) {
            stream = conn.getErrorStream();
        } else {
            stream = conn.getInputStream();
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, "utf-8"));

        StringBuilder response = new StringBuilder();
        String responseLine;

        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        System.out.println("\n===== AI RESPONSE =====");
        System.out.println(response);
    }
}