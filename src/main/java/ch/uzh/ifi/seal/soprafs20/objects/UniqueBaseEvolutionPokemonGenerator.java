package ch.uzh.ifi.seal.soprafs20.objects;

import java.util.*;

public class UniqueBaseEvolutionPokemonGenerator {

    private LinkedList<Integer> pokemons;

    private List<Integer> finalPokemons = new ArrayList<>();

    private int generation;

    /**
     * "Generates" an unique PokemonId
     */
    public UniqueBaseEvolutionPokemonGenerator(int generation) {
        this.generation = generation;
        this.refresh();
    }

    private void refresh() {
        int i = 0;
        if (i < generation) {
            finalPokemons.addAll(firstGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(secondGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(thirdGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(fourthGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(fifthGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(sixthGenPokemons);
            i++;
        }
        if (i < generation) {
            finalPokemons.addAll(seventhGenPokemons);
            i++;
        }
        this.pokemons = new LinkedList<>(this.finalPokemons);
        Collections.shuffle(this.pokemons);
    }

    /**
     * Get a unique pokemon id
     *
     * @return a unique pokemon id
     */
    public Integer get() {
        if (this.pokemons.isEmpty()) {
            this.refresh();
        }
        return this.pokemons.pop();
    }

    // Base evolutions of the first 151 Pokémon
    private final ArrayList<Integer> firstGenPokemons = new ArrayList<>(Arrays.asList(
            1, 4, 7, 10, 13, 16, 19, 21, 23, 25, 27, 29, 32, 35, 37, 39, 41, 43, 46, 48, 50, 52, 54, 56, 58, 63, 66, 69,
            72, 74, 77, 79, 81, 83, 84, 86, 88, 90, 92, 95, 96, 98, 100, 102, 104, 106, 107, 108, 109, 111, 113, 114,
            115, 116, 118, 120, 122, 123, 124, 125, 126, 127, 128, 129, 131, 132, 133, 151, 150, 147, 146, 145, 144,
            143, 142, 140, 138, 137
    ));

    private final ArrayList<Integer> secondGenPokemons = new ArrayList<>(Arrays.asList(
            152, 155, 158, 161, 163, 165, 167, 169, 170, 172, 173, 174, 175, 177, 179, 182, 298, 438, 186, 187, 190,
            191, 193, 194, 196, 197, 198, 200, 201, 360, 204, 206, 207, 208, 209, 211, 212, 213, 214, 215, 216, 218,
            220, 222, 223, 225, 458, 228, 230, 231, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245,
            246, 249, 250, 251
    ));

    private final ArrayList<Integer> thirdGenPokemons = new ArrayList<>(Arrays.asList(
            252,255,258,261,263,265,270,273,276,278,280,283,285,287,290,293,296,298,299,300,302,303,304,307,309,311,312,
            313,314,406,315,316,318,320,322,325,327,328,331,333,335,336,337,338,339,341,343,345,347,349,351,352,353,355,
            357,433,358,359,360,361,363,366,369,370,371,374,377,378,379,380,381,382,383,384,385,386
    ));

    private final ArrayList<Integer> fourthGenPokemons = new ArrayList<>(Arrays.asList(
            387, 390, 393, 396, 399, 401, 403, 406, 407, 408, 410, 412, 415, 417, 418, 420, 422, 424, 425, 427, 429,
            430, 431, 433, 434, 436, 438, 439, 440, 441, 442, 443, 446, 447, 449, 451, 453, 455, 456, 458, 459, 461,
            462, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481,
            482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494
    ));

    private final ArrayList<Integer> fifthGenPokemons = new ArrayList<>(Arrays.asList(
            495,498,501,504,506,509,511,513,515,517,519,522,524,527,529,531,532,535,538,539,540,543,546,548,550,551,554,
            556,557,559,561,562,564,565,566,568,570,572,574,577,580,582,585,587,588,590,592,594,595,597,599,602,605,607,
            610,613,615,616,618,619,621,622,624,626,627,629,631,632,633,636,638,639,640,641,642,643,644,645,646,647,648,
            649
    ));

    private final ArrayList<Integer> sixthGenPokemons = new ArrayList<>(Arrays.asList(
            650, 653, 656, 659, 661, 664, 667, 669, 672, 674, 676, 677, 679, 682, 684, 686, 688, 690, 692, 694, 696,
            698, 700, 701, 702, 703, 704, 708/*, 710*/, 712, 714, 716, 717, 718, 719, 720, 721
    ));

    private final ArrayList<Integer> seventhGenPokemons = new ArrayList<>(Arrays.asList(
            722,725,728,731,734,736,739,741,742,/*744,*/746,747,749,751,753,755,757,759,761,764,765,766,767,769,771,772,774,
            775,776,777,778,779,780,781,782,785,786,787,788,789,793,794,795,796,797,798,799,800,801,802/*,803,805,806,807
            ,808*/
    ));

    /*
    private final ArrayList<Integer> eighthGenPokemons = new ArrayList<>(Arrays.asList(
            222, 810, 813, 816, 819, 821, 824, 827, 829, 831, 833, 835, 837, 838, 840, 843, 845, 846, 848, 850, 852,
            854, 856, 859, 868, 870, 871, 872, 874, 875, 876, 877, 878, 885
    ));
     */

}
