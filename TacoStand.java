import java.util.Scanner;





public class TacoStand {

	public static void main(String[] args) {
		
		float[][]matrice = {
				{1,1,1,1,0,0,0,5},
				{1,1,0,0,1,0,0,1},
				{0,1,1,0,0,1,0,4},
				{1,0,1,0,0,0,1,3},
				{1,1,1,0,0,0,0,0}	
		};
		
		Scanner in=new Scanner(System.in);
		int n=in.nextInt();
		for(int i=0;i<n;i++){
			int s=in.nextInt();
			int m=in.nextInt();
			int r=in.nextInt();
			int b=in.nextInt();
			
			matrice[0][7]=s;
			matrice[1][7]=b;
			matrice[2][7]=r;
			matrice[3][7]=m;
			
			Matrice init =new Matrice(5, 8);
			init.setMatrice(matrice);
			Simplex simplex = new Simplex(init);
			Optimum optimum = new Optimum(simplex);
			
			System.out.println((int)optimum.calcul_Optimum()*-1);
		}
		
		

	}
	public static class elementMatrice {
	    
	    private int ligne;
	    private int colonne;
	    private float valeur;

	    public elementMatrice(int ligne, int colonne, float valeur)
	    {
	        this.ligne = ligne;
	        this.colonne = colonne;
	        this.valeur = valeur;
	    }

	    public elementMatrice() {
	    }
	        

	    public int getColonne() {
	        return colonne;
	    }

	    public int getLigne() {
	        return ligne;
	    }

	    public void setLigne(int ligne) {
	        this.ligne = ligne;
	    }

	    public void setColonne(int colonne) {
	        this.colonne = colonne;
	    }

	    public void setValeur(float valeur) {
	        this.valeur = valeur;
	    }

	    public float getValeur() {
	        return valeur;
	    }          
	}
	public static class Matrice {

	    private float[][] matrice;
	    private int nbLignes = 0;
	    private int nbColonnes = 0;

	    public Matrice(int nbLignes, int nbColonnes){
	        this.nbLignes = nbLignes;
	        this.nbColonnes = nbColonnes;
	        this.matrice=new float[nbLignes][nbColonnes];
	    }

	    public float[][] getMatrice() {
	        return matrice;
	    }

	    public int getNbColonnes() {
	        return nbColonnes;
	    }

	    public int getNbLignes() {
	        return nbLignes;
	    }

	    public void setMatrice(float[][] matrice) {
	        for(int i = 0 ; i < nbLignes;i++){
	            for(int j = 0 ; j < nbColonnes;j++)
	            {
	              this.matrice[i][j] = matrice[i][j];   
	            }
	        }
	        
	    }

	    public void setNbColonnes(int nbColonnes) {
	        this.nbColonnes = nbColonnes;
	    }

	    public void setNbLignes(int nbLignes) {
	        this.nbLignes = nbLignes;
	    }
	}
	public static class Optimum {

		private Simplex simplex;
		public Optimum(Simplex simplex){
			this.simplex=simplex;
		}
		public float calcul_Optimum(){
			
			while(simplex.chercheMax(simplex.getMatrice()) > 0)
	        {
	            simplex.resolutionProbleme();
	        }
	        
			return simplex.getMatrice().getMatrice()[simplex.getMatrice().getNbLignes()-1][simplex.getMatrice().getNbColonnes()-1];
		}
	}
	public final static class Simplex {
		
		private Matrice matriceDepart;
	    private Matrice matrice;
		private int jMaxDerniereLigne; //Numero de colone de la valeur max        
		private elementMatrice pivot = new elementMatrice();
		
		public Simplex(Matrice matrice){
			this.matrice=matrice;
		}

	        //Fonction pour resoudre le probleme par la premiere methode
		public void resolutionProbleme(){
	            float max = chercheMax(this.matrice);

	            if(max>0){
	                cherchePivot(this.matrice);
	                soustractionLigne(matrice);
	                divisionLignePivot(matrice);
	            }
		}
		//Cherche le maximum sur la derniere ligne
		public float chercheMax(Matrice maMatrice){
			float maximum = 0;		
			int tailleMatrice = maMatrice.getMatrice().length;
			
			for(int j=0;j<maMatrice.getMatrice()[0].length;j++)
			{
				if (maMatrice.getMatrice()[tailleMatrice-1][j] > maximum)
				{
					maximum = maMatrice.getMatrice()[tailleMatrice-1][j];
					jMaxDerniereLigne=j;
				}
			}
	   		return maximum;
		}
		
		//Cherche le pivot 1ere methode
		public void cherchePivot(Matrice maMatrice){
			float calculPivot;
			float calcul;
	                calculPivot = 9999;
	                elementMatrice pivotTemp = null;
			for (int i=0;i<maMatrice.getMatrice().length-1;i++)
			{
				calcul= (float)(maMatrice.getMatrice()[i][maMatrice.getMatrice()[0].length-1])/(maMatrice.getMatrice()[i][jMaxDerniereLigne]);
	                        if(calcul<calculPivot && calcul > 0)
				{
					calculPivot=calcul;
					pivotTemp = new elementMatrice();
	                                pivotTemp.setColonne(jMaxDerniereLigne);
	                                pivotTemp.setLigne(i);
	                                pivotTemp.setValeur(maMatrice.getMatrice()[i][jMaxDerniereLigne]);
				}
			}
	                this.pivot = pivotTemp;
		}
		//Fonction qui divise la ligne du pivot par la valeur du pivot
		public void divisionLignePivot(Matrice maMatrice){
			for(int j=0; j<maMatrice.getMatrice()[0].length;j++)
			{
				maMatrice.getMatrice()[pivot.getLigne()][j]=(maMatrice.getMatrice()[pivot.getLigne()][j])/pivot.getValeur();
			}
		}
		
		//Fonction qui soustrait chaque ligne a la ligne du pivot multipliÃ© par un coefficient
		public void soustractionLigne(Matrice maMatrice){
			for (int i=0;i<maMatrice.getMatrice().length;i++)
			{
				if(i!=pivot.getLigne())
				{
					float coeffSoustraction = (maMatrice.getMatrice()[i][pivot.getColonne()])/pivot.getValeur();
					for (int j=0;j<maMatrice.getMatrice()[0].length;j++)
					{
						maMatrice.getMatrice()[i][j]= (maMatrice.getMatrice()[i][j])-(coeffSoustraction*maMatrice.getMatrice()[pivot.getLigne()][j]);
					}
				}
			}
	    }	
	    /**
		 * Setter matrice
		 */
	    public Matrice getMatrice() {
	        return matrice;
	    }

	    public Matrice getMatriceDepart() {
	        return matriceDepart;
	    }       
	}
}

