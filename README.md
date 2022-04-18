## Dev Journal

I'm going to start a dev journal here too, the practice has been helpful on my other projects.
This repository exists as a kind of "clean room" or "sandbox" in which to practice working with technologies
that I come across in my professional work. The things I'd like to optimize for are:

 - Ease of use while leveraging the Spring Framework 
 - Clarity of how the system operates
 - Providing incentive for the code to grow in a desirable way

I've chosen a food delivery system as the domain because most people have had some kind of food delivery experience,
so it's familiar, and there is enough complexity to allow me to dive as deep as I wish. 

**4/17/2022**

I'm starting with three different sub-domains; Order, Delivery, and Restaurant. Orders will keep and manage all information
regarding the state of a given order; what was purchased, who purchased it and where is it going. Delivery will deal with
all aspects of getting the food from the restaurant to a customer. Restaurant will handle menus, physical address of the restaurant,
whether or not the restaurant is open / closed, etc.

I'm trying to think of events (in this case, messages coming in from a kafka topic) as 1st class domain citizens. If I'm
to test from the outside in, tests will begin by sending a message in, and expecting something to pop out.
