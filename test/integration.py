#!/usr/bin/python

import unittest
import requests

TARGET="http://localhost:8080"

class SimpleTests(unittest.TestCase):
    def test_get(self):
        r = requests.get(TARGET + "/test")
        self.assertEqual(r.status_code, 200)

        out = {k: v for k, v in [s.split(":", 1) for s in r.text.split("\n") ]}
        self.assertEqual(out["Request"], "/test")

if __name__ == "__main__":
    unittest.main()
