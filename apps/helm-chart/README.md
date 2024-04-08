# apps/helm-chart

This directory contains the [Helm](https://helm.sh/) chart for deploying Nx Cloud CE on a [Kubernetes](https://kubernetes.io/) cluster.

As an all-in-one package, this chart includes Bitnami's Helm Charts for MongoDB (DB) and NGINX (Reverse Proxy).

Everything is already configured, but you can fine-tune settings and use an external MongoDB connection (e.g. from MongoDB Community Operator).

## Prerequisites

- Kubernetes 1.16+
- Helm 3+

## Packaging the Helm Chart

The chart can be packaged using:

```shell script
nx build apps/helm-chart
```

It will download dependencies and create a tgz package at `apps/helm-chart/dist`.

## Install from source using Helm

For development purpose, you can install this chart using `helm`:

```shell script
helm install [release name] apps/helm-chart/src [-n [namespace]] [--set... | -f values.yaml]
```

## Configuration

Refer to [`values.yaml`](./src/values.yaml) for the exhaustive list of settings.
