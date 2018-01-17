========OVERVIEW========

This program utilizes a Minimum Priority Queue to store and organize user-defined data about various cars. The data structure supports methods for inserting, removing, updating, and retrieving the minimum object based on user-defined search parameters.

Each Car added to the PQ is given an associated Key and each Key holds the Car's index in both the Price and Miles PQ as well as its VIN. They Keys are stored in a one-dimension array and sorted by VINs using the quick-sort algorithm. Binary search inherently has a worst-case runtime of O(log n), and because we keep a reference to the Car's indices in each PQ, we can find it in constant time once the matching VIN in the Key array is found.

When updating a Car's information, its position in the PQ needs to be rebalanced. This means that, for each change, the Car's associated Key needs to be updated to contain the correct indices. Because the Keys are sorted by VIN number, which is immutable once declared, we avoid having to re-sort the entire array again. We simply perform another binary search for the VIN and update the references to the PQs.

========MINPQ CLASS========
To allow users to search for a Car either by lowest number of miles or the lowest price, two MinPQ's were created. Each holds a String variable 'type', denoting which parameter ("miles" or "price") the PQ is sorted by. In doing this, only one method has to be written perform a binary search because we ask for the type of PQ in the parameters.

The class implements standard methods of any Priority Queue (sink, swim, insert, remove, etc), which gives it a time complexity of O(log n) for insert and remove.

========CAR CLASS========
Each Car object holds data concerning its VIN, color, price, make, model, and the number of miles on its odometer.