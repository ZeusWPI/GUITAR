package gent.zeus.guitar.ext

import gent.zeus.guitar.ServerError

class OrderingError(missingValueName: String) : ServerError(
    "model fillers are in wrong order (value `$missingValueName` is still missing)",
    null,
)
