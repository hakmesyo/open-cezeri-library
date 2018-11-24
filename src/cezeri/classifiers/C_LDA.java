package cezeri.classifiers;




/**
 * Copyright (c) 2010, Dr. Wolfgang Lenhard, Psychometrica.de
 * All rights reserved.
 *
 * This code serves for calculating a linear discriminant analysis (LDA) and it is based on the
 * tutorial of Kardi Teknomo (http://people.revoledu.com/kardi/tutorial/LDA/index.html). You will
 * need JAMA (A Java Matrix Package; http://math.nist.gov/javanumerics/jama/) to run this routines.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Dr. Wolfgang Lenhard, 2010
 * @version 0.1, 07/31/2010
 *
 * Quotation:
 * Lenhard, W. (2010). Realisation of Linear Discriminant Analysis in Java. Bibergau: Psychometrica.
 * 
 * 
 */
import java.util.ArrayList;

import Jama.Matrix;

public class C_LDA {
	private double[][] groupMean;
	private double[][] pooledInverseCovariance;
	private double[] probability;
	private ArrayList<Integer> groupList = new ArrayList<Integer>();

	/**
	 * Calculates a linear discriminant analysis (LDA) with all necessary
	 * 
	 * @param data
	 *            The data as double array. The array must have the same size as
	 *            the group array
	 * @param group
	 *            The membership in the different groups
	 * @param p
	 *            Set to true, if the probability estimation should be based on
	 *            the real group sizes (true), or if the share of each group
	 *            should be equal
	 */
	
	public C_LDA(double[][] tr,boolean p){
		double[][] d=new double[tr.length][tr[0].length-1];
		int[] g=new int[tr.length];
		for (int i = 0; i <tr.length; i++) {
			g[i]=(int)tr[i][tr[0].length-1];
			//println("group:"+g[i]);
			for (int j = 0; j < d[0].length; j++) {
				d[i][j]=tr[i][j];
				//println("data:"+d[i][j]);
			}
		}
		LDAM(d,g,p);
	}
	
	@SuppressWarnings("unchecked")
	public void LDAM(double[][] d, int[] g, boolean p) {
		// check if data and group array have the same size
		if (d.length != g.length)
			return;

		double[][] data = new double[d.length][d[0].length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i].length; j++) {
				data[i][j] = d[i][j];
			}
		}
		int[] group = new int[g.length];
		for (int j = 0; j < g.length; j++) {
			group[j] = g[j];
		}

		double[] globalMean;
		double[][][] covariance;

		// determine number and label of groups
		for (int i = 0; i < group.length; i++) {
			if (!groupList.contains(group[i])) {
				groupList.add(group[i]);
			}
		}

		// divide data into subsets
		ArrayList<double[]>[] subset = new ArrayList[groupList.size()];
		for (int i = 0; i < subset.length; i++) {
			subset[i] = new ArrayList<double[]>();
			for (int j = 0; j < data.length; j++) {
				if (group[j] == groupList.get(i)) {
					subset[i].add(data[j]);
				}
			}
		}

		// calculate group mean
		groupMean = new double[subset.length][data[0].length];
		for (int i = 0; i < groupMean.length; i++) {
			for (int j = 0; j < groupMean[i].length; j++) {
				groupMean[i][j] = getGroupMean(j, subset[i]);
			}
		}

		// calculate global mean
		globalMean = new double[data[0].length];
		for (int i = 0; i < data[0].length; i++) {
			globalMean[i] = getGlobalMean(i, data);
		}

		// correct subset data
		for (int i = 0; i < subset.length; i++) {
			for (int j = 0; j < subset[i].size(); j++) {
				double[] v = subset[i].get(j);

				for (int k = 0; k < v.length; k++)
					v[k] = v[k] - globalMean[k];

				subset[i].set(j, v);
			}
		}

		// calculate covariance
		covariance = new double[subset.length][globalMean.length][globalMean.length];
		for (int i = 0; i < covariance.length; i++) {
			for (int j = 0; j < covariance[i].length; j++) {
				for (int k = 0; k < covariance[i][j].length; k++) {
					for (int l = 0; l < subset[i].size(); l++)
						covariance[i][j][k] += (subset[i].get(l)[j] * subset[i]
								.get(l)[k]);

					covariance[i][j][k] = covariance[i][j][k]
							/ subset[i].size();
				}
			}
		}

		// calculate pooled within group covariance matrix and invert it
		pooledInverseCovariance = new double[globalMean.length][globalMean.length];
		for (int j = 0; j < pooledInverseCovariance.length; j++) {
			for (int k = 0; k < pooledInverseCovariance[j].length; k++) {
				for (int l = 0; l < subset.length; l++) {
					pooledInverseCovariance[j][k] += ((double) subset[l].size() / (double) data.length)
							* covariance[l][j][k];
				}
			}
		}

		pooledInverseCovariance = new Matrix(pooledInverseCovariance).inverse()
				.getArray();

		// calculate probability for different groups
		this.probability = new double[subset.length];
		if (!p) {
			double prob = 1.0d / groupList.size();
			for (int i = 0; i < groupList.size(); i++) {
				this.probability[i] = prob;
			}
		} else {
			for (int i = 0; i < subset.length; i++) {
				this.probability[i] = (double) subset[i].size()
						/ (double) data.length;
			}
		}
	}

	private double getGroupMean(int column, ArrayList<double[]> data) {
		double[] d = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			d[i] = data.get(i)[column];
		}

		return getMean(d);
	}

	private double getGlobalMean(int column, double data[][]) {
		double[] d = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			d[i] = data[i][column];
		}

		return getMean(d);
	}

	/**
	 * Calculates the discriminant function values for the different groups
	 * 
	 * @param values
	 * @return
	 */
	public double[] getDiscriminantFunctionValues(double[] values) {
		double[] function = new double[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			double[] tmp = matrixMultiplication(groupMean[i],
					pooledInverseCovariance);
			function[i] = (matrixMultiplication(tmp, values))
					- (.5d * matrixMultiplication(tmp, groupMean[i]))
					+ Math.log(probability[i]);
		}

		return function;
	}

	/**
	 * Calculates the discriminant function values for the different groups based on Mahalanobis distance
	 * 
	 * @param values
	 * @return
	 */
	// TODO has to be tested yet
	public double[] getMahalanobisDistance(double[] values) {
		double[] function = new double[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			double[] dist = new double[groupMean[i].length];
			for (int j = 0; j < dist.length; j++)
				dist[j] = values[j] - groupMean[i][j];
			function[i] = matrixMultiplication(matrixMultiplication(dist,
					this.pooledInverseCovariance), dist);
		}

		return function;
	}

	/**
	 * Predict the membership of an object to one of the different groups based on Mahalanobis distance
	 * 
	 * @param values
	 * @return the group
	 */
	// TODO has to be tested yet
	public int predictM(double[] values) {
		int group = -1;
		double max = Double.NEGATIVE_INFINITY;
		double[] discr = this.getMahalanobisDistance(values);
		for (int i = 0; i < discr.length; i++) {
			if (discr[i] > max) {
				max = discr[i];
				group = groupList.get(i);
			}
		}

		return group;
	}

	/**
	 * Calculates the probability for the membership in the different groups
	 * 
	 * @param values
	 * @return the probabilities
	 */
	public double[] getProbabilityEstimates(double[] values) {
		// TODO
		return new double[] {};
	}

	/**
	 * Returns the weight for the linear fisher's discrimination functions
	 * 
	 * @return the weights
	 */
	public double[] getFisherWeights() {
		// TODO
		return new double[] {};
	}

	/**
	 * Predict the membership of an object to one of the different groups.
	 * 
	 * @param values
	 * @return the group
	 */
	public int predict(double[] values) {
		int group = -1;
		double max = Double.NEGATIVE_INFINITY;
		double[] discr = this.getDiscriminantFunctionValues(values);
		for (int i = 0; i < discr.length; i++) {
			if (discr[i] > max) {
				max = discr[i];
				group = groupList.get(i);
			}
		}

		return group;
	}

	/**
	 * Multiplies two matrices and returns the result as a double[][]-array.
	 * Please not, that the number of rows in matrix a must be equal to the
	 * number of columns in matrix b
	 * 
	 * @param a
	 *            the first matrix
	 * @param b
	 *            the second matrix
	 * @return the resulting matrix
	 */
	@SuppressWarnings("unused")
	private double[][] matrixMultiplication(final double[][] matrixA,
			final double[][] matrixB) {
		int rowA = matrixA.length;
		int colA = matrixA[0].length;
		int colB = matrixB[0].length;

		double c[][] = new double[rowA][colB];
		for (int i = 0; i < rowA; i++) {
			for (int j = 0; j < colB; j++) {
				c[i][j] = 0;
				for (int k = 0; k < colA; k++) {
					c[i][j] = c[i][j] + matrixA[i][k] * matrixB[k][j];
				}
			}
		}

		return c;
	}

	/**
	 * Multiplies two matrices and returns the result as a double[]-array.
	 * Please not, that the number of rows in matrix a must be equal to the
	 * number of columns in matrix b
	 * 
	 * @param a
	 *            the first matrix
	 * @param b
	 *            the second matrix
	 * @return the resulting matrix
	 */
	private double[] matrixMultiplication(double[] matrixA, double[][] matrixB) {

		double c[] = new double[matrixA.length];
		for (int i = 0; i < matrixA.length; i++) {
			c[i] = 0;
			for (int j = 0; j < matrixB[i].length; j++) {
				c[i] += matrixA[i] * matrixB[i][j];
			}
		}

		return c;
	}

	/**
	 * Multiplies two matrices and returns the result as a double (the second
	 * matrix is transposed automatically). Please note, that the number of rows
	 * in matrix a must be equal to the number of columns in matrix b
	 * 
	 * @param a
	 *            the first matrix
	 * @param b
	 *            the second matrix
	 * @return the resulting matrix
	 */
	private double matrixMultiplication(double[] matrixA, double[] matrixB) {

		double c = 0d;
		for (int i = 0; i < matrixA.length; i++) {
			c += matrixA[i] * matrixB[i];
		}

		return c;
	}

	/**
	 * Transposes a matrix
	 * 
	 * @param matrix
	 *            the matrix to transpose
	 * @return the transposed matrix
	 */
	@SuppressWarnings("unused")
	private double[][] transpose(final double[][] matrix) {
		double[][] trans = new double[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				trans[j][i] = matrix[i][j];
			}
		}

		return trans;
	}

	/**
	 * Transposes a matrix
	 * 
	 * @param matrix
	 *            the matrix to transpose
	 * @return the transposed matrix
	 */
	@SuppressWarnings("unused")
	private double[][] transpose(final double[] matrix) {
		double[][] trans = new double[1][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			trans[0][i] = matrix[i];
		}

		return trans;
	}

	/**
	 * Returns the mean of the given values. On error or empty data returns 0.
	 * 
	 * @param values
	 *            The values.
	 * @return The mean.
	 * @since 1.5
	 */
	public static double getMean(final double[] values) {
		if (values == null || values.length == 0)
			return Double.NaN;

		double mean = 0.0d;

		for (int index = 0; index < values.length; index++)
			mean += values[index];

		return mean / (double) values.length;
	}

	/**
	 * Test case with the original values from the tutorial of Kardi Teknomo
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		int[] group = { 1, 1, 1, 1, 2, 2, 2 };
//		double[][] data = { { 2.95, 6.63 }, { 2.53, 7.79 }, { 3.57, 5.65 },
//				{ 3.16, 5.47 }, { 2.58, 4.46 }, { 2.16, 6.22 }, { 3.27, 3.52 } };
//		int[] group = {1,2,1,2,1,1,2};
//		double[][] data = { { 2.53, 7.79 }, { 2.58, 4.46 }, { 3.57, 5.65 }, { 3.27, 3.52 },
//				{ 3.16, 5.47 },{ 2.95, 6.63 }, { 2.16, 6.22 } };
		//int[] group = { 1, 1, 1, 1, 2, 2, 2 };
//		double[][] trData = { 
//				{ 2.95, 6.63,2.34,0 },
//				{ 2.58, 4.46,3.43,1 }, 
//				{ 2.53, 7.79,2.41,0 }, 
//				{ 2.16, 6.22,4.44,1 }, 
//				{ 3.57, 5.65,2.55,0 },
//				{ 3.27, 3.52,3.01,1 }, 
//				{ 3.16, 5.47,2.78,0 } 
//				};
		
		double[][] tr={
//				{-0.199,-0.321,-0.079,-0.094,2.018,0 },
//				{-0.199,-0.321,-0.079,-0.198,0.766,0 },
//				{-0.199,-0.321,-0.079,-0.484,0.781,0 },
				{-0.199,-0.321,-0.079,3.054,-0.35,0 },
				{-0.199,-0.321,-0.079,1.819,-0.544,0 },
				{-0.199,-0.321,-0.079,0.794,-0.497,0 },
				{-0.199,3.088,-0.079,-0.462,0.694,0 },
				{-0.199,-0.321,-0.079,-0.551,1.024,0 },
				{-0.199,-0.321,-0.079,-0.489,0.932,0 },
				{-0.199,-0.321,-0.079,-0.653,0.741,0 },
				{-0.199,3.088,-0.079,-0.697,0.764,0 },
				{-0.144,3.088,-0.079,-0.609,0.833,0 },
				{-0.199,-0.321,-0.079,-0.673,0.682,0 },
				{2.182,-0.321,-0.079,-0.417,0.687,0 },
				{-0.199,-0.321,-0.079,-0.719,0.584,0 },
				{-0.199,-0.321,-0.079,-0.159,0.7,0 },
				{-0.199,3.088,-0.079,-0.597,0.181,0 },
				{-0.199,-0.321,-0.079,-0.665,-1.661,0 },
				{-0.199,-0.321,-0.079,-0.534,0.554,0 },
				{1.019,-0.321,-0.079,-0.661,0.582,0 },
				{4.73,-0.321,-0.079,-0.664,-1.618,0 },
				{-0.199,-0.321,-0.079,-0.14,0.84,0 },
				{-0.199,-0.321,-0.079,-0.537,-1.666,0 },
				{-0.199,-0.321,-0.079,-0.655,0.945,0 },
				{-0.088,-0.321,-0.079,0.349,0.651,0 },
				{-0.199,-0.321,-0.079,0.516,0.703,0 },
				{-0.088,-0.321,-0.079,-0.452,0.485,0 },
				{-0.199,-0.321,-0.079,-0.448,0.311,0 },
				{-0.144,-0.321,-0.079,-0.606,0.326,0 },
				{-0.199,-0.321,-0.079,-0.374,0.39,0 },
				{-0.199,-0.321,-0.079,-0.659,0.562,0 },
				{-0.199,-0.321,-0.079,-0.706,0.625,0 },
				{-0.199,-0.321,-0.079,-0.7,0.017,0 },
				{-0.199,-0.321,-0.079,-0.382,-1.652,0 },
				{-0.199,-0.321,-0.079,-0.592,0.576,0 },
				{-0.199,-0.321,-0.079,-0.495,1.105,0 },
				{-0.144,-0.321,-0.079,-0.716,-1.601,0 },
				{-0.199,-0.321,-0.079,-0.714,0.748,0 },
				{-0.199,-0.321,-0.079,-0.521,-1.616,0 },
				{0.132,-0.321,-0.079,-0.338,1.748,0 }, 
				{-0.199,-0.321,-0.079,1.382,0.048,0 },
				{-0.199,-0.321,-0.079,0.085,0.019,0 },
				{-0.199,-0.321,-0.079,-0.736,1.09,0 },
				{-0.199,-0.321,-0.079,-0.738,-1.517,0 },
				{-0.199,-0.321,-0.079,-0.738,1.013,0 },
				{-0.199,-0.321,-0.079,0.5,-1.456,0 },
				{-0.199,-0.321,-0.079,-0.235,0.469,0 },
				{-0.199,-0.321,-0.079,0.833,0.303,0 },
				{-0.199,-0.321,-0.079,-0.642,0.654,0 },
				{-0.199,-0.321,-0.079,-0.709,0.777,0 },
				{-0.199,-0.321,-0.079,-0.704,-1.605,0 },
				{-0.199,-0.321,-0.079,-0.653,1.763,0 },
				{-0.199,3.088,-0.079,-0.71,0.741,0 },
				{-0.199,-0.321,-0.079,-0.71,0.871,0 },
				{-0.199,-0.321,-0.079,-0.521,0.511,0 },
				{-0.199,-0.321,-0.079,-0.595,1.045,0 },
				{-0.199,-0.321,-0.079,-0.599,0.923,0 },
				{-0.199,-0.321,-0.079,-0.126,0.567,0 },
				{-0.199,-0.321,-0.079,-0.481,0.088,0 },
				{-0.199,-0.321,-0.079,-0.467,0.036,0 },
				{10.38,-0.321,-0.079,0.484,-0.192,0 },
				{1.351,-0.321,-0.079,-0.403,-1.514,0 },
				{0.52,-0.321,-0.079,-0.284,0.488,0 },
				{-0.199,-0.321,-0.079,-0.729,1.098,0 },
				{-0.033,-0.321,-0.079,-0.736,1.034,0 },
				{-0.144,-0.321,-0.079,-0.736,0.966,0 },
				{-0.199,-0.321,-0.079,-0.331,-1.62,0 },
				{-0.199,-0.321,-0.079,-0.391,-1.623,0 },
				{-0.199,3.088,-0.079,-0.533,0.704,0 }, 
				{-0.199,-0.321,-0.079,0.411,0.213,0 },
				{-0.199,-0.321,-0.079,-0.641,0.318,0 },
				{-0.199,-0.321,-0.079,-0.375,0.41,0 },
				{-0.199,-0.321,-0.079,-0.716,-1.597,0 },
				{-0.199,-0.321,-0.079,-0.724,-1.598,0 },
				{-0.199,3.088,-0.079,-0.708,1.132,0 },
				{-0.199,-0.321,-0.079,-0.678,0.912,0 },
				{-0.199,-0.321,-0.079,-0.495,-1.59,0 },
				{-0.199,-0.321,-0.079,-0.679,1.095,0 },
				{-0.199,-0.321,-0.079,0.885,0.091,0 },
				{0.243,3.088,-0.079,-0.531,0.305,0 },
				{-0.199,-0.321,-0.079,-0.462,0.204,0 },
				{0.575,-0.321,-0.079,3.335,-0.22,0 },
				{-0.033,-0.321,-0.079,0.241,-1.684,0 },
				{-0.199,-0.321,-0.079,0.886,-1.682,0 },
				{-0.199,-0.321,-0.079,-0.594,0.88,0 },
				{-0.199,-0.321,-0.079,-0.635,-1.646,0 },
				{-0.144,-0.321,-0.079,-0.619,0.887,0 },
				{0.077,-0.321,-0.079,1.102,-1.679,1 },
				{0.465,-0.321,-0.079,1.26,0.124,1 },
				{-0.033,-0.321,-0.079,0.21,0.41,1 },
				{-0.199,-0.321,-0.079,2.574,0.566,1 },
				{-0.199,-0.321,-0.079,2.574,0.566,1 },
				{-0.199,-0.321,-0.079,0.741,-1.654,1 },
				{-0.199,-0.321,-0.079,0.146,0.386,1 },
				{-0.199,-0.321,-0.079,0.52,0.404,1 },
				{-0.199,-0.321,-0.079,-0.61,-0.301,1 },
				{-0.199,3.088,-0.079,-0.218,0.494,1 },
				{-0.199,-0.321,-0.079,-0.687,0.359,1 },
				{-0.199,-0.321,-0.079,-0.703,0.579,1 },
				{-0.199,-0.321,-0.079,-0.594,0.652,1 },
				{-0.088,-0.321,-0.079,1.411,0.88,1 },
				{-0.199,-0.321,-0.079,0.093,0.051,1 },
				{-0.199,-0.321,-0.079,-0.459,0.209,1 },
				{-0.199,-0.321,-0.079,-0.645,-1.542,1 },
				{-0.199,-0.321,-0.079,-0.696,0.653,1 },
				{-0.199,-0.321,-0.079,-0.706,-1.534,1 },
				{-0.144,-0.321,-0.079,0.647,0.45,1 },
				{-0.199,-0.321,-0.079,0.169,1.465,1 },
				{-0.199,-0.321,-0.079,-0.452,0.491,1 },
				{-0.199,-0.321,-0.079,-0.653,0.642,1 },
				{-0.199,-0.321,-0.079,-0.599,0.432,1 },
				{-0.199,-0.321,-0.079,-0.46,0.482,1 },
				{-0.199,-0.321,-0.079,0.766,0.238,1 },
				{0.021,-0.321,-0.079,1.058,0.273,1 },
				{-0.199,-0.321,-0.079,1.551,0.401,1 },
				{-0.088,-0.321,-0.079,1.325,-1.437,1 },
				{0.243,-0.321,-0.079,2.085,-0.0040,1 },
				{-0.144,-0.321,-0.079,-0.317,0.33,1 },
				{-0.199,-0.321,-0.079,0.041,-1.625,1 },
				{-0.199,-0.321,-0.079,0.342,-1.611,1 },
				{-0.199,-0.321,-0.079,-0.057,-1.627,1 },
				{-0.199,-0.321,-0.079,2.912,0.168,1 },
				{-0.199,-0.321,-0.079,0.77,0.351,1 },
				{-0.199,3.088,-0.079,1.817,0.163,1 },
				{-0.199,3.088,-0.079,2.135,0.29,1 },
				{-0.144,-0.321,-0.079,1.14,-1.672,1 },
				{-0.199,-0.321,-0.079,1.461,-1.672,1 },
				{-0.144,-0.321,-0.079,0.708,-1.657,1 },
				{-0.199,-0.321,-0.079,-0.075,0.369,1 },
				{0.354,-0.321,-0.079,0.498,0.34,1 },
				{-0.199,-0.321,-0.079,-0.613,0.258,1 },
				{-0.199,-0.321,-0.079,-0.57,-1.66,1 },
				{-0.199,-0.321,-0.079,-0.667,-1.65,1 },
				{-0.199,-0.321,-0.079,0.269,-0.04,1 },
				{0.132,3.088,-0.079,0.117,-1.66,1 },
				{-0.199,-0.321,-0.079,-0.502,0.355,1 },
				{-0.199,-0.321,-0.079,0.405,0.458,1 },
				{-0.199,-0.321,-0.079,-0.252,-1.61,1 },
				{-0.199,-0.321,-0.079,-0.285,0.422,1 },
				{-0.199,-0.321,-0.079,-0.242,-1.637,1 },
				{-0.199,-0.321,-0.079,-0.338,0.614,1 },
				{-0.199,-0.321,-0.079,-0.571,-1.647,1 },
				{-0.199,-0.321,-0.079,-0.355,0.113,1 },
				{-0.199,-0.321,-0.079,-0.622,0.656,1 },
				{-0.199,-0.321,-0.079,-0.5,0.673,1 },
				{-0.199,-0.321,12.53,-0.598,-1.591,1 },
				{0.077,-0.321,-0.079,-0.633,-1.619,1 },
				{0.021,-0.321,-0.079,-0.717,-1.569,1 },
				{-0.199,-0.321,-0.079,5.481,0.394,1 },
				{-0.199,-0.321,-0.079,3.723,0.276,1 },
				{-0.199,-0.321,-0.079,1.749,0.349,1 },
//				{-0.199,-0.321,-0.079,-0.619,0.862,1 },
//				{-0.199,3.088,-0.079,-0.655,-1.171,1 },
//				{-0.199,-0.321,-0.079,-0.679,0.736,1 },
				{-0.199,-0.321,-0.079,0.847,0.399,1 },
				{-0.199,-0.321,-0.079,0.847,0.399,1 },
				{3.733,3.088,-0.079,-0.486,0.531,1 },
				{0.188,3.088,-0.079,0.671,0.542,1 },
				{-0.144,-0.321,-0.079,1.187,0.917,1 },
				{-0.199,-0.321,-0.079,-0.087,-1.634,1 },
				{-0.199,-0.321,-0.079,0.167,0.81,1 }

				
		};
		//println(data.length+":"+data[0].length);
		
		C_LDA test = new C_LDA(tr,true);
//		double[] t1={-0.199,-0.321,-0.079,-0.094,2.018 };
//		double[] t2={-0.199,-0.321,-0.079,-0.198,0.766 };
//		double[] t3={-0.199,-0.321,-0.079,-0.484,0.781 };

		double[] t1={-0.199,-0.321,-0.079,-0.619,0.862 };
//		double[] t2={-0.199,3.088,-0.079,-0.655,-1.171 };
//		double[] t3={-0.199,-0.321,-0.079,-0.679,0.736 };
//
//		double[] testData = { 2.81, 5.46,1.78 };
		
		//test
		//double[] values = test.getDiscriminantFunctionValues(testData);
//		double[] values = test.getDiscriminantFunctionValues(t1);
//		for(int i = 0; i < values.length; i++){
//			System.out.println("Discriminant function " + (i+1) + ": " + values[i]);	
//		}
//		
		System.out.println("Predicted group: " + test.predict(t1));
	}

//	private static void println(String str) {
//		System.out.println(str);
//	}
}
