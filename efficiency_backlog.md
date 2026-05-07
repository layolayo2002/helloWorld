# Energy Efficiency Backlog

## Complete List (All Opportunities)

| Priority | Focus Area | Opportunity | Status |
|----------|------------|-------------|--------|
| HIGH ✅ | Code-Level | ShoppingCart HashMap lookups (getTotalAmount) | PR #6 |
| HIGH ✅ | Code-Level | ShoppingCart HashMap lookups (getOriginalTotalAmount) | PR #6 |
| HIGH ✅ | Code-Level | ProductsDAO.addColor() duplicate query | Created PR |
| HIGH 🔴 | Data/I/O | OrdersDAO.insert() N+1 query problem (getColorNo/getSizeNo in loop) | IDENTIFIED |
| MEDIUM ✅ | Code-Level | RDBConnection static driver loading | PR #10 |
| MEDIUM ✅ | Code-Level | Customer type caching - eliminate reflection | PR #22 |
| MEDIUM 📋 | Data/I/O | Database connection pooling absent | Future |

## Current PR Status

1. **PR #6**: ShoppingCart optimization (entrySet + bitwise AND fix)
   - Tests: ✅ Passing
   - Status: Draft, awaiting review

2. **PR #10**: RDBConnection JDBC driver initialization
   - Tests: ✅ Passing
   - Status: Draft, awaiting review

3. **PR #22**: Customer type caching - eliminate reflection
   - Tests: ✅ Passing
   - Status: Draft, awaiting review

4. **PR #24**: addColor() optimization (single query instead of full-table scan)
   - Tests: ✅ Passing
   - Status: Draft, awaiting review
   - Branch: efficiency/addcolor-duplicate-query-fix

## New Opportunity Identified (May 7, 2026)

### OrdersDAO.insert() N+1 Query Problem

**Location**: `/src/layo/clo/service/OrdersDAO.java:48-58`

**Problem**: In the loop over `order.getOrderItemSet()`, the code calls:
- `pservice.getColorNo(item.getProduct().getColor())` → Database query
- `pservice.getSizeNo(item.getProduct().getSize())` → Database query

For an order with N items, this generates **2N database queries** just for color/size lookups.

**Impact**: 
- Database overhead: For order with 50 items = 100 extra DB queries
- Network traffic: 100 round-trips instead of 1-2 batched queries
- Estimated energy: 30-50% reduction possible

**Solution**: 
1. Pre-fetch all available colors and sizes once (2 queries total)
2. Cache in memory (HashMap: String → int)
3. Look up in cache during loop (no additional queries)

**Priority**: HIGH (potential 30-50% energy savings for order insertion)
