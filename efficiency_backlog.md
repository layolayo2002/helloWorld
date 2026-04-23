# Energy Efficiency Opportunities Backlog

## HIGH Priority

### 1. ShoppingCart.getTotalAmount() - Redundant HashMap Lookups
**File:** src/layo/clo/entity/ShoppingCart.java (lines 96-107)
**Issue:** Loop calls `cart.get(p)` inside iteration over keySet - O(n) hash lookups
**Energy Impact:** HIGH - Loops over all products, double-hashing each lookup
**Fix:** Use `cart.entrySet()` instead of `keySet()` to iterate and access value directly
**Measurement:** Execution time reduction (milliseconds for typical cart sizes)
**Status:** Ready to implement

### 2. ShoppingCart.getOriginalTotalAmount() - Same Issue
**File:** src/layo/clo/entity/ShoppingCart.java (lines 109-116)
**Issue:** Same redundant `cart.get(p)` in loop
**Energy Impact:** HIGH
**Fix:** Use entrySet() instead
**Status:** Ready to implement

### 3. ShoppingCart.getTotalQuantity() - Bitwise AND Bug
**File:** src/layo/clo/entity/ShoppingCart.java (line 88)
**Issue:** Uses single `&` (bitwise AND) instead of `&&` (logical AND)
- `if (q != null & q > 0)` should be `if (q != null && q > 0)`
- Bitwise operation is slower and wrong semantics
**Energy Impact:** MEDIUM - extra CPU cycles on bitwise operation
**Fix:** Change to `&&`
**Status:** Ready to implement

## MEDIUM Priority

### 4. Database Connection Pooling Absent
**Files:** OrdersDAO.java, ProductsDAO.java, CustomersDAO.java
**Issue:** Each database method calls `RDBConnection.getConnection()` - no connection pooling visible
**Energy Impact:** MEDIUM - repeated connection creation/teardown is expensive
**Fix:** Implement connection pooling (requires infrastructure work)
**Status:** Requires investigation of RDBConnection implementation

### 5. Eager Entity Creation in Loops
**File:** OrdersDAO.java
**Issue:** Creates new Customer/Order objects inside ResultSet loop even when they might not be needed
**Energy Impact:** LOW-MEDIUM - garbage collection pressure
**Fix:** Consider lazy initialization or batching
**Status:** Requires careful analysis

## LOW Priority

### 6. String toString() in Computations
**File:** ShoppingCart.java (line 121)
**Issue:** toString() calls methods like getTotalQuantity() during string building
**Energy Impact:** LOW - happens infrequently
**Fix:** Only call toString() when actually needed
**Status:** Best practice, not critical

---

## Measurement Strategy
- For ShoppingCart optimizations: Create microbenchmark with typical cart sizes (5-50 products)
- Use System.nanoTime() for wall-clock time measurements
- Compare before/after with 100 iterations
- Proxy metric: execution time reduction = energy reduction for CPU-bound code

## Notes
- Project is learning-focused (student project) - prioritize code clarity
- Small codebase (3K LOC) means optimizations have cumulative effect
- Consider education value vs. performance (readable code is important)
