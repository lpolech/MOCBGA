package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of non dominated random vector mutation.
 * Selects 3 random non dominated vectors and perform linear combination on them.
 */
public class NonDominatedRandomVectorMutation extends BaseMutation<Double, BaseProblemRepresentation> {

  /**
   * Performs a mutation, where a base individual
   * is a random non dominated individual
   *
   * @param population population to process
   * @param f weight of a difference of two vectors
   *          used during the mutation
   * @param genesToMutate list of genes to mutate
   * @param current index of an individual to mutate
   * @param populationSize size of the population
   * @param parameters set of parameters
   * @return mutant vector
   */
  @Override
  public List<Double> mutate(List<BaseIndividual<Double, BaseProblemRepresentation>> population, double f, List<Double> genesToMutate,
                             int current, int populationSize, ParameterSet<Double, BaseProblemRepresentation> parameters) {
    int r1, r2, r3;
    List<Double> x1, x2, x3;
    List<BaseIndividual<Double, BaseProblemRepresentation>> nondominated = getNondominated(population);

    int numGenes = population.get(current).getGenes().size();
    List<Double> mutant;
    r1 = parameters.random.nextInt(nondominated.size());
    x1 = nondominated.get(r1).getGenes();
    mutant = new ArrayList<>(x1);

    for (int r = 0; r < parameters.mutationRank; ++r) {
      do {
        r2 = parameters.random.nextInt(populationSize);
      } while (r2 == r1 || r2 == current);
      do {
        r3 = parameters.random.nextInt(populationSize);
      } while (r3 == r2 || r3 == r1 || r3 == current);
      x2 = population.get(r2).getGenes();
      x3 = population.get(r3).getGenes();
      for (int i = 0; i < numGenes; ++i) {
        mutant.set(i, mutant.get(i) + f * (x2.get(i) - x3.get(i)));
      }
    }

    return mutant;
  }

  @Override
  public List<Double> mutate(List<BaseIndividual<Double, BaseProblemRepresentation>> population, double TSPf, double KNAPf, List<Double> genesToMutate, int current, int populationSize, ParameterSet<Double, BaseProblemRepresentation> parameters) {
    return null;
  }

  protected List<BaseIndividual<Double, BaseProblemRepresentation>> getNondominated(
      List<BaseIndividual<Double, BaseProblemRepresentation>> population) {

    List<BaseIndividual<Double, BaseProblemRepresentation>> nondominatedSolutions = new ArrayList<>();

    for (BaseIndividual<Double, BaseProblemRepresentation> individual : population) {
      if (individual.isNotDominatedBy(population)) {
        nondominatedSolutions.add(individual);
      }
    }

    return nondominatedSolutions;
  }

}
