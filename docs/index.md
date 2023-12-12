---
title: Overview
description: "Introducing the Rock-Paper-Scissors simulation application"
menu: Overview
order: 0
---

{% include ddc-abbreviations.md %}

## Introduction

We're all familiar with the game of Rock-Paper-Scissors (RPS). What you may not know, however, is that there are actual ecosystems that can be viewed in terms of RPS. It seems that nature sometimes plays RPS on an evolutionary timescale.

For example, there's a type of lizard---the common side-blotched lizard---where the competition between males for breeding partners resembles an evolutionary game of RPS: 

* Orange-throated males are large and aggressive, each attempting to mate with several females in a relatively large territory, chasing other males away.

* Blue-throated males are also aggressive, but not as large as the orange-throated males, and attempt to control and mate with females in smaller territories. Because of their smaller size and smaller territories, they do not have as many opportunities for reproduction as the orange-throated males, and have fewer offspring as a result.

* Yellow-throated males do not attempt to control territory, and they are easily chased away by the blue-throated males. However, they have a trick up their sleeves: They resemble female side-blotched lizards. Because the orange-throated males can't guard their larger territories as effectively as the blue-throated males guard theirs, the yellow-throated males can sneak in and mate with females in orange-throated males' territories, thus stealing these breeding opportunities from the orange-throated males. On the other hand, since the blue-throated males can guard their smaller territories more closely, they aren't as easily fooled by the yellow-throated males.

While there are other factors at play (in particular, the mating preferences of the females), it is useful to view the competition between the males as a form of RPS, where yellow is dominated (in reproductive success) by blue, which is dominated by orange, which is in turn dominated by yellow, just as rock &lt; paper &lt; scissors &lt; rock. This type of relationship is called _intransitive_, since it doesn't follow the transitive property, in which knowing that $A > B$, and that $B > C$, also tells us that $A > C$.

## Extension

Of course, RPS can be extended to more than 3 choices---most famously in the case of Rock-Paper-Scissors-Lizard-Spock, invented by Sam Kass and Karen Bryla, and popularized by the television show, "The Big Bang Theory". While it can appear complicated to expand the concept in this fashion, the complication lies mostly in the invention of the associated gestures, and in coming up with the stories behind the relationships (e.g., rock crushes scissors, rock smashes lizard, scissors cut paper, scissors decapitate lizard, and so on). However, constructing a set of intransitive relationships for some number of choices $N$ (where $N \ge 3$) is quite simple: arrange the $N$ options around a circle, and declare that each option is dominated by the next $\lfloor (N - 1) / 2 \rfloor$ options found clockwise around the circle from it, and that it in turn dominates the next $\lfloor (N - 1) / 2 \rfloor$ options counterclockwise from it. (With an even number of breeds, each breed has another one directly across from it in the circle; competition between individuals of breeds directly opposite each other always ends in a tie.)

## Simulation

Among other approaches, we can simulate a very idealized intransitive ecosystem as follows:

1. Fill a grid or lattice completely with agents (objects, values, etc.) representing the participants, where every cell of the grid (or point of the lattice) is occupied by exactly one agent. (The initial distribution of breeds can be random, or it might follow some deliberate scheme for experimental purposes.) This grid or lattice is the _terrain_ of the ecosystem.

2. At each iteration of the simulation:

    1. Select one of the agents at random.
   
    2. Select (again, at random) one of the selected agent's 4 immediately adjacent neighbors, to form a competing pair.

        We may choose to let our terrain edges wrap around, so that an agent on one edge is considered adjacent to an agent on the opposite edge.[^torus] Otherwise, if the first agent is on an edge, we'll complete the competing pair by choosing from the first agent's 3 immediately adjacent neighbors (instead of 4). Similarly, if the first agent is in the corner, and we're not using wrapping (also called _periodic_) boundaries, the other half of the competing pair will be chosen from the first agent's 2 immediately adjacent neighbors.

    3. Compare the breeds of the two agents in the selected pair: 

        * If the breeds are the same, then there is no change in populations in this iteration.
       
        * On the other hand, if the breeds are different, then the agent losing the competition is replaced by a copy of the winner.
       
            For example: if we select an agent at random, which happens to be of the rock breed; and we select one of that agent's neighbors at random, and the neighbor is of the paper breed; then the rock agent is removed from the simulation (since "paper covers rock"), and is replaced by a copy of the paper agent.

3. Repeat as long as desired, or until all agents in the simulation are of a single breed (this condition is called the "absorbing mode").

It may surprise you to find out that such a simple simulation is actually pretty good at modeling the population dynamics in some intransitive ecosystems---in particular, extensive work has been done in modeling an _E. coli_ ecosystem with this approach, with good results (e.g. ["Local dispersal promotes biodiversity in a real-life game of rock–paper–scissors"](pdf/feldman-rock-scissors-paper.pdf), Feldman et al., 2002).

[^torus]: Periodic boundaries give us a _toroidal_ topology: Imagine taking a flexible sheet of paper or other material, and rolling it into a cylinder, so that the left and right edges curve around to touch each other. Then, roll the cylinder vertically, so that the circles at the top and bottom edges of the cylinder connect. The doughnut-shaped result is a _torus_. 

## Mixing

Optionally, we might use such a simulation model to explore the effects of mixing---i.e., periodically redistributing some subset of the entire population, either randomly or according to some strategy. In physical experiments, it has been seen that such mixing can have a significant effect on the short- and long-term population dynamics---and we see similar results in simulation models (For an example, see ["Emergent Behavior of Rock-Paper-Scissors Game"](pdf/kircher.pdf), Kircher, K., 2006.)

In a grid-based simulation, random mixing can easily be done by periodically selecting a pair of agents at random (i.e., not necessarily neighbors) and swapping their positions in the grid. (This is functionally identical to temporarily rewiring connections in a lattice-based model.) The probability of such swaps is an important experimental control parameter.

## Android application

{% include assignment-info.md %}

When you use the provided link to accept this assignment, you will start with a repository containing code that implements a grid-based intransitive ecosystem simulation, starting with 3--9 breeds, in an Android app. In fact, the app is already very close to complete: It implements virtually all the functionality described in ["Simulation"](#simulation) and ["Mixing"](#mixing), with user-selectable settings for controlling the terrain size and topology (that is, periodic or closed boundaries), and the number of breeds initially populating the terrain. There are working controls to start (or resume), pause, and reset the simulation. There are also controls for the user to set the simulation run speed and the probability of periodic selection and swapping of randomly chosen pairs of individuals.

Of course, it wouldn't be a very interesting or challenging assignment if the app were complete. In fact, the simulation code itself is quite complete, but the app itself has a number of missing pieces, outlined in the sections below.

## Critical deficiencies

1. _There is no visual display of the simulation as it runs._ There is a `View` subclass intended for this purpose, but the method that would render the terrain in this view has only a skeletal implementation. (See ["Animation rendering"](render-animation.md).)

2. _There is no display of key statistics,_ such as the current number of individuals of each breed, and the current number of extant (surviving) breeds. (See ["Simulation statistics"](simulation-statistics.md).)

3. _The user settings for swap probability and simulation run speed are not yet being used._ The method used to start or resume the simulation has parameters for these purposes, but the values currently supplied as arguments in the invocation of this method do not incorporate the user settings. (See ["Additional settings"](speed-swap-settings.md).)

4. _The app is currently using the default launcher icon._ No app development project is truly complete without a custom launcher icon. (See ["Launcher icon"](launcher-icon.md).)

## Stretch goals

Beyond the above deficiencies, there are some functional areas where the app could (optionally) be improved: 

1. Most critically, the layout used by the app's primary fragment (screen) is not at all suited for display in landscape orientation. (See ["Landscape orientation"](landscape.md).)

2. For performance reasons, it is desirable to change the visual rendering of individuals in the simulation from circles (which, as you will see, is part of the specification for the first item in the list of deficiencies, above) to squares, when the display size of each individual is below a given threshold. (See ["Animation rendering: Stretch goal"](render-animation.md#stretch-goal).)

## Execution

### Specifications 

Each of the pages in this module outlines one of the deficiencies or stretch goals. However, the definitive specifications and instructions for completing most of them are actually contained in `TODO` comments in the code itself. Thus, a significant part of the challenge is to read these comments, as well as the surrounding code, to understand the changes required. Even before you receive the repository, you can view the code and comments in the [Javadoc](api/) documentation: when looking at the detailed Javadoc information for any of the methods, clicking on the method name will take you to a formatted code listing that includes the `TODO` comments. (There are similar links for viewing the `TODO` comments in the layout file for the primary fragment, the `TODO` comments in the settings resource file, and the `TODO` comments in the Android manifest.)

### Version control

In executing the necessary additions to the code, you and your team are expected to make effective use of Git version control. The details of this will be left to you, but we strongly recommend that (among other practices) you use separate feature- or fix-oriented branches for completing the different elements, and that you use pull requests for merging completed work into the `main` branch. Don't waste a lot of time dealing with merge conflicts that can easily be avoided (or at least minimized)!

### TODO comments

Most of the detailed specifications and instructions for the tasks in this assignment are spelled out in `TODO` comments in the source code, which are made available (in advance) in these pages and the accompanying Javadoc documentation. To complete this assignment successfully, you will need to read these comments carefully; also, we strongly advise you to change the `TODO` prefix (or remove the comment completely) after completing each task, so that your `TODO` tool window in IntelliJ isn't polluted by tasks you've already completed. 

### Instructor consultation

This project is intended to give you an opportunity to demonstrate your current capacity for real-world development work. To that end, you should view your instructor as the technical lead for the organization: If you're stuck on a technical issue, need clarification of some requirements, need help in resolving a Git problem, etc., request a consultation with your instructor. In this case, your instructor will not tell you how to fix any given problem, but will try to give you useful direction on how to approach the problem.
