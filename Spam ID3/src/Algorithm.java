public class Algorithm 
{
	public double calcEntropy(double posFactor, double negFactor)
	{
		//Logarithm Obvious Values
		if(posFactor == 0.0) return 0.0;
		else if(negFactor == 0.0) return 0.0;
		
		double resultPos = posFactor * (Math.log10(posFactor)/Math.log10(2));
		double resultNeg = negFactor * (Math.log10(negFactor)/Math.log10(2));
		
		return -resultPos -resultNeg;
	}
}
