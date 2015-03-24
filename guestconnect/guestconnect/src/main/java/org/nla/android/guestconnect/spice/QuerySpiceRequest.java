package org.nla.android.guestconnect.spice;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

// Attention !! Cette classe ne doit pas être une classe interne du Context
public class QuerySpiceRequest extends GoogleHttpClientSpiceRequest<Result> {

	public QuerySpiceRequest() {
		super(Result.class);
	}

	@Override
	public Result loadDataFromNetwork() throws Exception {

		// Code à exécuter de manière asynchrone
		// Retourner le résultat de la requête

		try {

			// HttpURLConnection connection = getOkHttpClient().open(
			// new URI("http://www.google.de").toURL());
			// InputStream in = null;
			// try {
			// // Read the response.
			// in = connection.getInputStream();
			// } finally {
			// if (in != null) {
			// in.close();
			// }
			// }

			HttpRequest request = getHttpRequestFactory()//
					.buildGetRequest(new GenericUrl("http://www.google.de"));
			HttpResponse response = request.execute();

			// request.setParser( new JacksonFactory().createJsonObjectParser()
			// );
			// return request.execute().parseAs( getResultType()
			//
			// HttpURLConnection cnx = (HttpURLConnection) new URL(
			// "http://www.google.de").openConnection();
			// cnx.setConnectTimeout(5000);
			// cnx.setReadTimeout(3000);
			// cnx.setDefaultUseCaches(false);
			// cnx.connect();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		Result toReturn = new Result();
		toReturn.setId("zeea");
		toReturn.setContent("nico@bob.fr");
		return toReturn;
	}
}