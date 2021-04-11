import java.util.Vector;

public class CrimeSum {
	Vector<Crime> crimeSet;
	double score;
	int vehicleTheft;
	int arson;
	int theft;
	int robbery;
	int violence;
	double latitude;
	double longitude;
	
	CrimeSum(Vector<Crime> crimeSet, double score, int vehicleTheft, int arson, int theft, int robbery, int violence, double la, double lo){
		this.crimeSet = crimeSet;
		this.score = score;
		this.arson = arson;
		this.theft = theft;
		this.robbery = robbery;
		this.violence = violence;
		this.latitude = la;
		this.longitude = lo;
	}
	
}
