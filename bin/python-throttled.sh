#!/usr/bin/env bash
export OMP_NUM_THREADS=1
export MKL_NUM_THREADS=1
export OPENBLAS_NUM_THREADS=1
export NUMEXPR_NUM_THREADS=1
export TORCH_NUM_THREADS=1
PYTHON_BIN="$(dirname "$(readlink -f "$0")")/../.venv/bin/python"
CPULIMIT_BIN="$(command -v cpulimit)"
if [ -x "$CPULIMIT_BIN" ]; then
    exec nice -n 10 "$CPULIMIT_BIN" -l 40 -m -f -q -- "$PYTHON_BIN" "$@"
fi
exec nice -n 10 "$PYTHON_BIN" "$@"
