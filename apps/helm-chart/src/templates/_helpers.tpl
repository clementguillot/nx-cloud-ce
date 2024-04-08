{{/* vim: set filetype=mustache: */}}
{{/*
Create server name and version as used by the chart label.
Truncated at 52 chars because Deployment label 'server-revision-hash' is limited
to 63 chars and it includes 10 chars of hash and a separating '-'.
*/}}
{{- define "nx-cloud-ce.server.fullname" -}}
{{- printf "%s-%s" (include "nx-cloud-ce.fullname" .) .Values.server.name | trunc 52 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create the default port of the server.
*/}}
{{- define "nx-cloud-ce.server.port" -}}
{{- default "80" .Values.server.service.servicePortHttp -}}
{{- end -}}

{{/*
Create the MongoDB connection string of the server.
*/}}
{{- define "nx-cloud-ce.server.database.connection-string" -}}
{{- if .Values.mongodb.enabled }}
valueFrom:
  secretKeyRef:
    name: {{ template "nx-cloud-ce.name" . }}-mongodb-svcbind-0
    key: uri
{{- else }}
valueFrom:
  secretKeyRef:
    name: {{ .Values.server.database.connectionStringSecretName | quote }}
    key: {{ .Values.server.database.connectionStringSecretKeyRef | quote }}
{{- end -}}
{{- end -}}
