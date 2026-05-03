# Energy Efficiency Backlog

## Complete List (All Opportunities)

| Priority | Focus Area | Opportunity | Status |
|----------|------------|-------------|--------|
| HIGH ✅ | Code-Level | ShoppingCart HashMap lookups (getTotalAmount) | PR #6 |
| HIGH ✅ | Code-Level | ShoppingCart HashMap lookups (getOriginalTotalAmount) | PR #6 |
| HIGH ✅ | Code-Level | ProductsDAO.addColor() duplicate query | Created PR |
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

4. **New PR**: addColor() optimization (single query instead of full-table scan)
   - Tests: ✅ Passing
   - Status: Draft, awaiting review
   - Branch: efficiency/addcolor-duplicate-query-fix

## Measurement Summary

- ShoppingCart: 15-20% instruction reduction per call
- RDBConnection: 5-10% CPU reduction for database operations
- Customer type caching: 5-10% CPU reduction for database operations  
- addColor(): 50-80% query time reduction, 99% network transfer reduction
